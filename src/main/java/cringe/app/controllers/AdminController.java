package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
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
        return "admin/index";
    }

    @PostMapping("/new_game")
    public RedirectView newGame(@RequestParam String title) {
        Game game = new Game();
        game.setTitle(title);
        game = gameRepository.save(game);

        return new RedirectView("/admin/game/" + game.getId());
    }

    @GetMapping("/game/{id}")
    public String newGame(@PathVariable int id, Model model) {
        Game game = gameRepository.findGameById(id);
        model.addAttribute("gameDetails", game);
        model.addAttribute("game", game);
        return "admin/edit_game";
    }

    @PostMapping(value="/update/{id}")
    public String editGame(@PathVariable int id, @ModelAttribute("gameDetails") Game game, Model model) {
        Game original = gameRepository.findGameById(id);
        if(game.getDescription() != null) {
            original.setDescription(game.getDescription());
        }
        if(game.getPrice() != 0.0) {
            original.setPrice(game.getPrice());
        }
        if(game.getArtifact() != null) {
            original.setArtifact(game.getArtifact());
        }
        if(game.getIcon() != null) {
            original.setIcon(game.getIcon());
        }

        gameRepository.save(original);
        // TODO(evanSpendlove): Add more as we add more attributes of a game

        model.addAttribute("games", gameRepository.findAll());
        return "admin/index";
    }

//    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, SQLException {
        Artifact artifact = new Artifact();
        artifact.setData(new SerialBlob(file.getBytes()));
        artifact.setFileName(file.getOriginalFilename());
        artifact.setContentType(file.getContentType());
        artifact = artifactRepository.save(artifact);
    }


    @GetMapping("/orders")
    public String orders(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        if(!user.hasRole("admin")) {
            // Access denied
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "status"));

        model.addAttribute("orders", orders);
        return "admin/orders";
    }
}
