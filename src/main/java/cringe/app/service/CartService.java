package cringe.app.service;

import cringe.app.component.CartStore;
import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
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
    private CartStore cartStore;

    public Cart getCart(Principal principal) {
        if (principal == null) {
            if (cartStore.getCart() == null) {
                cartStore.setCart(new Cart());
            }
            return cartStore.getCart();
        } else {
            User user = userRepository.findByUsername(principal.getName());
            return user.getCart();
        }
    }

    public void saveCart(Principal principal, Cart cart) {
        if (principal == null) {
            cartStore.setCart(cart);
        } else {
            User user = userRepository.findByUsername(principal.getName());
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

    public void sync(Principal principal) {
        if (principal == null) {
            throw new NullPointerException("principal cannot be null");
        }

        if (cartStore.getCart().getGames().isEmpty()) {
            return;
        }

        User user = userRepository.findByUsername(principal.getName());
        Cart dbCart = user.getCart();
        List<Game> dbCartGames = dbCart.getGames();

        boolean dirty = false;
        for (Game game : cartStore.getCart().getGames()) {
            if (dbCartGames.stream().map(g -> g.getId() == game.getId()).findAny().isEmpty()) {
                dirty = true;
                dbCartGames.add(game);
            }
        }

        if (dirty) {
            cartRepository.save(dbCart);
        }


    }

}
