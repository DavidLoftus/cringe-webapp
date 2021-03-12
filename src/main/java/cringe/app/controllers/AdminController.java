package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public String editGame(@PathVariable int id,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) Float price,
                           Model model) {
        Game game = gameRepository.findGameById(id);

        if (StringUtils.isEmpty(title)) {
            game.setTitle(title);
        }

        if (StringUtils.isEmpty(description)) {
            game.setDescription(description);
        }

        if (price != null) {
            game.setPrice(price);
        }

        return editGame(id, model);
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


    @GetMapping("/orders")
    public String allOrders(Principal principal, Model model) {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "status"));

        model.addAttribute("orders", orders);
        return "admin/orders";
    }
}
