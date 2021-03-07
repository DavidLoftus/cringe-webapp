package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Controller
public class ShopController {

    @Autowired
    public ArtifactRepository artifactRepository;

    @Autowired
    public GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", gameRepository.findAll());
        return "index";
    }

<<<<<<< HEAD
    @GetMapping("/cart")
    public String viewCart(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());

        // TODO: Remove this once we have a default admin user with a cart
        // Use: For any default user created in data.sql
        if(user.getCart() == null) {
            Cart cart = new Cart();
            user.setCart(cart);
            cartRepository.save(cart);
        }

        model.addAttribute("user", user);
        model.addAttribute("totalCost", cartRepository.getTotalCost(user.getCart().getId()));

        return "cart";
    }

    @PostMapping("/cart/add")
    public RedirectView addToCart(Principal principal, @RequestParam int id) {
        User user = userRepository.findByUsername(principal.getName());

        // TODO: Remove this once we have a default admin user with a cart
        // Use: For any default user created in data.sql
        if(user.getCart() == null) {
            Cart cart = new Cart();
            user.setCart(cart);
            cartRepository.save(cart);
        }

        Optional<Game> maybeGame = gameRepository.findById(id);

        if (maybeGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Game game = maybeGame.get();
        Cart cart = user.getCart();
        if (!cart.getGames().contains(game)) {
            cart.getGames().add(game);
            cartRepository.save(cart);
        }

        return new RedirectView("/cart");
    }

    @GetMapping("/cart/checkout")
    public String checkout(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("totalCost", cartRepository.getTotalCost(user.getCart().getId()));

        return "checkout";
    }

    @PostMapping("/cart/checkout/complete")
    public RedirectView checkoutComplete(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        if(user.getCart() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Cart cart = user.getCart();

        // User now owns these games
        Set<Game> userGames = user.getGames();
        for(Game g : cart.getGames()) {
            userGames.add(g);
        }
        user.setGames(userGames);

        Order order = new Order(new Date(), user, cart);
        orderRepository.save(order);

        Cart emptyCart = new Cart();
        user.setCart(emptyCart);
        cartRepository.save(emptyCart);

        return new RedirectView("/orders");
    }

    @GetMapping("/orders")
    public String orders(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("orders", orderRepository.findOrdersByUser(user));
        return "orders";
    }

    @GetMapping("/game/new")
    public String newGame() {
        return "new_game";
    }

=======
>>>>>>> 55337c816ce76d823e9a47841c9889e5fdf3cec7
    @GetMapping("/artifact/{id}/**")
    @ResponseBody
    public ResponseEntity<Resource> getArtifact(@PathVariable int id) throws SQLException {
        final Artifact artifact = artifactRepository.findArtifactById(id);
        if (artifact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Resource resource = new InputStreamResource(artifact.getData().getBinaryStream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, artifact.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + artifact.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/game/{id}")
    public String viewGame(Principal principal, @PathVariable int id, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("game", game);
        if(user.getGames().contains(game)) {
            model.addAttribute("owns_game", true);
        } else {
            model.addAttribute("owns_game", false);
        }
        return "view_game";
    }

    @GetMapping("/game/{id}/buy")
    public String buyGame(@PathVariable int id, Model model) {
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("game", game);
        return "buy_game";
    }

    @GetMapping("/game/{id}/play")
    public String playGame(@PathVariable int id, Model model) {
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("game", game);
        return "jsdos";
    }
}
