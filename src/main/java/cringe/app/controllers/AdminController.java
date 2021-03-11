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
    public String adminPortal() {
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
    public String newGame(@PathVariable int id) {
        return "admin/edit_game";
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
    public String allOrders(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        // TODO(evanSpendlove): re-enable after we have an admin a/c
        /*
        if(!user.hasRole("admin")) {
            // Access denied
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
         */

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "status"));

        model.addAttribute("orders", orders);
        return "admin/orders";
    }
}
