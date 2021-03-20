package cringe.app.service;

import cringe.app.component.SessionStore;
import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private SessionStore sessionStore;

    public Cart getCart(Principal principal) {
        if (principal == null) {
            if (sessionStore.getCart() == null) {
                sessionStore.setCart(new Cart());
            }
            return sessionStore.getCart();
        } else {
            User user = userRepository.findByUsername(principal.getName());
            return user.getCart();
        }
    }

    public void saveCart(Principal principal, Cart cart) {
        if (principal == null) {
            sessionStore.setCart(cart);
        } else {
            cartRepository.save(cart);
        }
    }

    public void checkout(Principal principal) {
        if (principal == null) {
            throw new NullPointerException("principal cannot be null");
        }

        User user = userRepository.findByUsername(principal.getName());

        Cart cart = user.getCart();

        // User now owns these games
        user.getGames().addAll(cart.getGames());

        List<Purchase> purchases = new ArrayList<>();
        for(Game g: cart.getGames()) {
            Purchase p = new Purchase(g);
            purchases.add(p);
            purchaseRepository.save(p);
        }

        Order order = new Order(new Date(), user, purchases);
        orderRepository.save(order);

        Cart emptyCart = new Cart();
        user.setCart(emptyCart);
        cartRepository.save(emptyCart);

        // Delete old cart.
        cartRepository.delete(cart);
    }

    private boolean hasGame(Collection<Game> games, int id) {
        for (Game game : games) {
            if (game.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void sync(Principal principal) {
        if (principal == null) {
            throw new NullPointerException("principal cannot be null");
        }

        if (sessionStore.getCart() == null || sessionStore.getCart().getGames().isEmpty()) {
            return;
        }

        User user = userRepository.findByUsername(principal.getName());
        Cart dbCart = user.getCart();
        List<Game> dbCartGames = dbCart.getGames();

        boolean dirty = false;
        for (Game game : sessionStore.getCart().getGames()) {
            if (!hasGame(user.getGames(), game.getId()) && !hasGame(dbCartGames, game.getId())) {
                dirty = true;
                dbCartGames.add(game);
            }
        }

        if (dirty) {
            cartRepository.save(dbCart);
        }
    }

}
