package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import java.util.List;

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

    @GetMapping
    public String adminPortal(Model model) {
        model.addAttribute("games", gameRepository.findAll());
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

    public enum UploadType {
        RELEASE,
        ICON,
        BANNER,
    }

    @PostMapping("/game/{id}/upload")
    public void uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file, @RequestParam UploadType type) throws IOException, SQLException {
        Game game = gameRepository.findGameById(id);

        Artifact artifact = new Artifact();
        artifact.setData(new SerialBlob(file.getBytes()));
        artifact.setFileName(file.getOriginalFilename());
        artifact.setContentType(file.getContentType());
        artifact = artifactRepository.save(artifact);

        switch (type) {
            case RELEASE -> game.setRelease(artifact);
            case ICON -> game.setIcon(artifact);
        }
        gameRepository.save(game);
    }

    /*
        Problem: Modelling the relationship of: Seller -> Game
        - Each game has a seller
        - Each seller has multiple games
        - Each purchase can comprise of multiple games at certain prices

        Solution:
        - Seller {one2many} Game
        - Order {one2many} Purchase
        - Purchase has a game id, title, price
        - Order has a generateReceipt() method that is called for the orders.html page.

        Benefits:
        - Stores purchases only once
        - Handles single and multiple purchases equally well
        - Can handle querying total purchases per game, per user, etc. really well
     */


    @GetMapping("/orders")
    public String allOrders(Principal principal, Model model) {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "status"));

        model.addAttribute("orders", orders);
        return "admin/orders";
    }
}
