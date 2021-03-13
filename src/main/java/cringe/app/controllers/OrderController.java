package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String orders(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("orders", orderRepository.findOrdersByUser(user));

        // Simulate payment processing
        for(Order o: orderRepository.findOrdersByUser(user)) {
            if(o.getStatus() == Order.Status.processing) {
                orderRepository.updateStatus(o.getId(), Order.Status.completed);
            }
        }

        // TODO(evanSpendlove): Remove these print statements
        for(Order o: orderRepository.findOrdersByUser(user)) {
            System.out.println("Order: " + o.getId() + ", status: " + o.getStatus());
            System.out.println("Order: " + o.getId() + ", receipt: " + o.generateReceipt());
            System.out.println("Order: " + o.getId() + ", totalCost: " + o.computeTotalCost());
        }

        System.out.println(user.getGames());

        return "orders";
    }

    @PostMapping("/refund")
    @ResponseStatus(value = HttpStatus.OK)
    public void refund(Principal principal, @RequestParam int id) {
        User user = userRepository.findByUsername(principal.getName());

        Order order = orderRepository.findOrderById(id);
        order.setStatus(Order.Status.refunded);

        // User no longer owns these games
        Set<Game> games = user.getGames();
        for(Purchase p : order.getPurchases()) {
            games.remove(p.getGame());
        }

        user.setGames(games);
        orderRepository.updateStatus(id, Order.Status.refunded);
    }

}
