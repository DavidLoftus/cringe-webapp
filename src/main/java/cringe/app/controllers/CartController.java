package cringe.app.controllers;

import cringe.app.db.Cart;
import cringe.app.db.Game;
import cringe.app.db.GameRepository;
import cringe.app.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    public GameRepository gameRepository;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        Cart cart = cartService.getCart(principal);
        model.addAttribute("cart", cart);

        return "cart";
    }

    @PostMapping("/add")
    public RedirectView addToCart(Principal principal, @RequestParam int id) {
        Cart cart = cartService.getCart(principal);

        Optional<Game> maybeGame = gameRepository.findById(id);

        if (maybeGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Game game = maybeGame.get();
        if (!cart.getGames().contains(game)) {
            cart.getGames().add(game);
            cartService.saveCart(principal, cart);
        }

        return new RedirectView("/cart");
    }

    @PostMapping("/remove")
    @ResponseStatus(value = HttpStatus.OK)
    public void removeFromCart(Principal principal, @RequestParam int id) {
        Cart cart = cartService.getCart(principal);

        Optional<Game> maybeGame = gameRepository.findById(id);

        if (maybeGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Game game = maybeGame.get();
        if (cart.getGames().remove(game)) {
            cartService.saveCart(principal, cart);
        }
    }

    @GetMapping("/checkout")
    public String checkout(Principal principal, Model model) {
        Cart cart = cartService.getCart(principal);

        model.addAttribute("cart", cart);

        return "checkout";
    }

    @PostMapping("/checkout/complete")
    public RedirectView checkoutComplete(Principal principal) {
        cartService.checkout(principal);

        return new RedirectView("/orders");
    }

}
