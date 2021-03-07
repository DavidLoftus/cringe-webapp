package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    public GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());

        System.out.println(user.getCart());
        model.addAttribute("user", user);
        model.addAttribute("totalCost", cartRepository.getTotalCost(user.getCart().getId()));

        return "cart";
    }

    @PostMapping("/add")
    public RedirectView addToCart(Principal principal, @RequestParam int id) {
        User user = userRepository.findByUsername(principal.getName());

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

    @PostMapping("/remove")
    @ResponseStatus(value = HttpStatus.OK)
    public void removeFromCart(Principal principal, @RequestParam int id) {
        User user = userRepository.findByUsername(principal.getName());

        Optional<Game> maybeGame = gameRepository.findById(id);

        if (maybeGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Game game = maybeGame.get();
        Cart cart = user.getCart();
        if (cart.getGames().remove(game)) {
            cartRepository.save(cart);
        }
    }

    @GetMapping("/checkout")
    public String checkout(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("totalCost", cartRepository.getTotalCost(user.getCart().getId()));

        return "checkout";
    }

    @PostMapping("/checkout/complete")
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

}
