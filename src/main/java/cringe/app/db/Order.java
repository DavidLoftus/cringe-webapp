package cringe.app.db;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {
    public enum Status {
        processing, completed, refunded
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @OneToMany
    private List<Purchase> purchases;

    @Column
    private Status status;

    @Column
    private Date date;

    public Order() {
        this.status = Status.processing;
    }

    public Order(Date date, User user, List<Purchase> purchases) {
        setDate(date);
        setUser(user);
        setPurchases(purchases);
        this.status = Status.processing;
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

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String generateReceipt() {
        StringBuilder rec = new StringBuilder();
        for(Purchase p: getPurchases()) {
            rec.append(p.getGame().getTitle()).append(": €").append(String.format("%.2f", p.getPrice())).append("\n");
        }
        return rec.append("Total: €").append(String.format("%.2f", computeTotalCost())).toString();
    }

    public float computeTotalCost() {
        float cost = 0;
        for(Purchase p : getPurchases()) {
            cost += p.getPrice();
        }
        return cost;
    }
}
