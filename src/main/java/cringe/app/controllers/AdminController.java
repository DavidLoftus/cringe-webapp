package cringe.app.controllers;

import cringe.app.analytics.GameSale;
import cringe.app.db.*;
import cringe.app.util.UploadType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
            model.addAttribute("games", gameRepository.findAll());
            // TODO: Change: Non root users can only edit the games they sell.
           //  model.addAttribute("games", user.getGames());
        }
        return "admin/index.html";
    }

    @PostMapping("/new_game")
    public RedirectView newGame(@RequestParam String title) {
        Game game = new Game();
        game.setTitle(title);
        game = gameRepository.save(game);

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
            for (Game g : user.getGames()) {
                orders.addAll(orderRepository.findOrdersByGameId(g.getId()));
            }
        }

        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    @GetMapping("/analytics")
    public String analytics(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());

        List<Game> games = gameRepository.findAll();

        List<GameSale> gameSales = new ArrayList<>();
        for (Game g : games) {
            float total = 0f;
            if(purchaseRepository.purchaseByGameID(g.getId()).size() > 0) {
                total = purchaseRepository.totalPurchasesByGameId(g.getId());
            }
            gameSales.add(new GameSale(g, total));
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
        System.out.println(sales);

        Map<String, Float> totalPerGame = new HashMap<>();
        for (Game g :  games) {
           Float total = 0f;
           Map<Date, Float> curSales = sales.get(g);
           for(Date d : curSales.keySet()) {
               total += curSales.get(d);
           }
           totalPerGame.put(g.getTitle(), total);
        }

        // Need to format this for use in js.
        model.addAttribute("totalsPerGame", totalPerGame);
        model.addAttribute("gameSales", gameSales);

        return "admin/analytics";
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
