package cringe.app.db;

import javax.persistence.*;

@Entity
@Table(name="purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Game game;

    @Column
    private float price;

    public Purchase() {
    }

    public Purchase(Game g) {
        setGame(g);
        setPrice(g.getPrice());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
