package cringe.app.db;

import javax.persistence.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @OneToOne
    private Artifact artifact;

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

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact publicRelease) {
        this.artifact = publicRelease;
    }
}
