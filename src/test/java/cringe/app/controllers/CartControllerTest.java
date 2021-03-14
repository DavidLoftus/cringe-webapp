package cringe.app.controllers;

import cringe.app.db.*;
import cringe.app.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @Mock
    private GameRepository gameRepository;

    @Test
    void addToCart() {
        Principal principal = () -> "bob";

        Cart cart = new Cart();
        cart.setId(1);
        cart.setGames(new ArrayList<>());

        Game game = new Game();
        game.setId(1);
        game.setTitle("Doom");

        when(cartService.getCart(any())).thenReturn(cart);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        cartController.addToCart(principal, 1);

        verify(cartService, times(1)).saveCart(any(), argThat(x -> x.getGames().contains(game)));
    }

    @Test
    void removeFromCart() {
        Game game = new Game();
        game.setId(1);
        game.setTitle("Doom");

        Principal principal = () -> "bob";

        User bob = new User();
        bob.setId(1);
        bob.setRoles(Collections.emptySet());

        Cart cart = new Cart();
        cart.setId(1);
        cart.setGames(new ArrayList<>());
        cart.getGames().add(game);

        bob.setCart(cart);

        when(cartService.getCart(any())).thenReturn(cart);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        cartController.removeFromCart(principal, 1);

        verify(cartService, times(1)).saveCart(any(), argThat(x -> x.getGames().isEmpty()));
    }
}