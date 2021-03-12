package cringe.app.db;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @OneToOne(orphanRemoval = true)
    private Artifact release;

    @OneToOne(orphanRemoval = true)
    private Artifact icon;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> users;

    @Column
    private Float price;

    @Column
    private String description;

    public Game() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artifact getRelease() {
        return release;
    }

    public void setRelease(Artifact artifact) {
        this.release = artifact;
    }

    public Artifact getIcon() {
        return icon;
    }

    public void setIcon(Artifact icon) {
        this.icon = icon;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return getTitle();
    }
}
