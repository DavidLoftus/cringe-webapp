package cringe.app.db;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Game> games;

    public Cart() {
        this.games = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public int getTotalCost() {
        int cost = 0;
        for(Game g: getGames()) {
            cost += g.getPrice();
        }
        return cost;
    }
}
