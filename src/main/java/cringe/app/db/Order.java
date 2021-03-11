package cringe.app.db;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @OneToOne
    private Cart cart;

    public enum Status {
        pending, completed, refunded
    }

    @Column
    private Status status;

    @Column
    private Date date;

    @Column
    private int totalCost;

    public Order() {
        this.status = Status.pending;
    }

    public Order(Date date, User user, Cart cart) {
        setDate(date);
        setUser(user);
        setCart(cart);
        this.status = Status.pending;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        computeTotalCost();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void computeTotalCost() {
        int cost = 0;
        for(Game g: getCart().getGames()) {
            cost += g.getPrice();
        }
        setTotalCost(cost);
    }
}
