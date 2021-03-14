package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping("/totalsPerGame")
    public ResponseEntity<?> getTotalsPerGame() {
        List<Game> games = gameRepository.findAll();

        Map<Game, Map<Date, Float>> sales = new HashMap<>();
        for (Game g : games) {
            Map<Date, Float> salesForGame = new HashMap<>();
            List<Order> orders = orderRepository.findOrdersByGameId(g.getId());
            for(Order o : orders) {
                for(Purchase p : o.getPurchases()) {
                    if(p.getGame() == g) {
                        if(sales.containsKey(o.getDate())){
                            Float cur = salesForGame.get(o.getDate());
                            salesForGame.put(o.getDate(), cur + p.getPrice());
                        } else {
                            salesForGame.put(o.getDate(), p.getPrice());
                        }
                    }
                }
            }
            sales.put(g, salesForGame);
        }

        Map<String, Float> totalPerGame = new HashMap<>();
        for (Game g :  games) {
            Float total = 0f;
            Map<Date, Float> curSales = sales.get(g);
            for(Date d : curSales.keySet()) {
                total += curSales.get(d);
            }
            totalPerGame.put(g.getTitle(), total);
        }

        System.out.println("REST Query: " + totalPerGame);

        return new ResponseEntity<>(totalPerGame, HttpStatus.OK);
    }
}
