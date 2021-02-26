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
}
