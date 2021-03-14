package cringe.app.controllers;

import cringe.app.analytics.GameSale;
import cringe.app.db.*;
import cringe.app.util.UploadType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.util.StringUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @GetMapping
    public String adminPortal(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        if(user.hasRole("root")) {
            model.addAttribute("games", gameRepository.findAll());
        } else {
            model.addAttribute("games", user.getGamesSold());
        }
        return "admin/index.html";
    }

    @PostMapping("/new_game")
    public RedirectView newGame(Principal principal, @RequestParam String title) {
        Game game = new Game();
        game.setTitle(title);
        game = gameRepository.save(game);

        User user = userRepository.findByUsername(principal.getName());
        Set<Game> gamesSold = user.getGamesSold();
        gamesSold.add(game);
        user.setGamesSold(gamesSold);
        userRepository.save(user);

        return new RedirectView("/admin/game/" + game.getId());
    }

    @GetMapping("/game/{id}")
    public String editGame(@PathVariable int id, Model model) {
        model.addAttribute("game", gameRepository.findGameById(id));

        return "admin/edit_game";
    }

    @PostMapping("/game/{id}")
    public RedirectView editGame(@PathVariable int id,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) Float price,
                           @RequestParam(required = false) GameVisibility visibility,
                           Model model) {
        Game game = gameRepository.findGameById(id);

        if (!StringUtils.isEmpty(title)) {
            game.setTitle(title);
        }

        if (!StringUtils.isEmpty(description)) {
            game.setDescription(description);
        }

        if (price != null) {
            game.setPrice(price);
        }

        if (visibility != null) {
            game.setVisibility(visibility);
        }

        gameRepository.save(game);

        return new RedirectView("/admin/game/" + id);
    }

    @PostMapping("/game/{id}/upload")
    public RedirectView uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file, @RequestParam UploadType type) throws IOException, SQLException {
        Game game = gameRepository.findGameById(id);

        Artifact artifact = new Artifact();
        artifact.setData(new SerialBlob(file.getBytes()));
        artifact.setFileName(file.getOriginalFilename());
        artifact.setContentType(file.getContentType());
        artifact = artifactRepository.save(artifact);

        switch(type) {
            case RELEASE:
                game.setRelease(artifact);
                break;
            case ICON:
                game.setIcon(artifact);
                break;
            case BANNER:
                game.setBanner(artifact);
                break;
            case LOGO:
                game.setLogo(artifact);
                break;
            case BACKGROUND:
                game.setBackground(artifact);
                break;
        }

        gameRepository.save(game);

        return new RedirectView("/admin/game/" + id);
    }

    @GetMapping("/orders")
    public String orders(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());

        List<Order> orders = new ArrayList<>();
        if(user.hasRole("root")) {
            orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "status"));
        } else{
            for (Game g : user.getGamesSold()) {
                orders.addAll(orderRepository.findOrdersByGameId(g.getId()));
            }
        }

        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    @GetMapping("/analytics")
    public String analytics(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        List<Game> games = new ArrayList<>(user.getGamesSold());

        if(user.hasRole("root")) {
            games = gameRepository.findAll();
        }

        List<GameSale> gameSales = new ArrayList<>();
        for (Game g : games) {
            float total = 0f;
            if(purchaseRepository.purchaseByGameID(g.getId()).size() > 0) {
                total = purchaseRepository.totalPurchasesByGameId(g.getId());
            }
            gameSales.add(new GameSale(g, total));
        }

        model.addAttribute("userId", user.getId());
        model.addAttribute("gameSales", gameSales);

        return "admin/analytics";
    }


    @RequestMapping("/analytics/totalsPerGame")
    public ResponseEntity<?> getTotalsPerGame(@RequestParam("id") long userId) {
        User user = userRepository.findById(userId).get();
        List<Game> games = new ArrayList<>(user.getGamesSold());

        if(user.hasRole("root")) {
            games = gameRepository.findAll();
        }

        Map<Game, Map<Date, Float>> sales = new HashMap<>();
        for (Game g : games) {
            Map<Date, Float> salesForGame = new HashMap<>();
            List<Order> orders = orderRepository.findOrdersByGameId(g.getId());
            for(Order o : orders) {
                for(Purchase p : o.getPurchases()) {
                    if(p.getGame() == g) {
                        if(sales.containsKey(o.getDate())){
                            Float cur = salesForGame.get(o.getDate());
                            salesForGame.put(o.getDate(), cur + p.getPrice());
                        } else {
                            salesForGame.put(o.getDate(), p.getPrice());
                        }
                    }
                }
            }
            sales.put(g, salesForGame);
        }


        Map<String, Float> totalPerGame = new HashMap<>();
        Float totalPie = 0f;
        for (Game g :  games) {
            Float total = 0f;
            Map<Date, Float> curSales = sales.get(g);
            for(Date d : curSales.keySet()) {
                total += curSales.get(d);
            }
            totalPie += total;
            totalPerGame.put(g.getTitle(), total);
        }

        System.out.println("REST Query: " + totalPerGame);

        if(totalPie > 0.0) {
            for (Game g : games) {
                totalPerGame.put(g.getTitle(), (totalPerGame.get(g.getTitle()) / totalPie));
            }
        }

        return new ResponseEntity<>(totalPerGame, HttpStatus.OK);
    }

    @PostMapping("/refundOrder")
    @ResponseStatus(value = HttpStatus.OK)
    public void refund(@RequestParam int id) {
        Order order = orderRepository.findOrderById(id);
        order.setStatus(Order.Status.refunded);

        User user = userRepository.findByUsername(order.getUser().getUsername());

        // User no longer owns these games
        Set<Game> games = user.getGames();
        for(Purchase p : order.getPurchases()) {
            games.remove(p.getGame());
        }

        user.setGames(games);
        orderRepository.updateStatus(id, Order.Status.refunded);
    }

}
