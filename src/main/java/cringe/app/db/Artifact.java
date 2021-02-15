package cringe.app.db;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name="releases")
public class Artifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column
    private Blob data;

    @Column
    private String fileName;

    @Column
    private String contentType;

    public Artifact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getData() {
        return data;
    }

    public void setData(Blob data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String fileInfo) {
        this.contentType = fileInfo;
    }

    public String getArtifactPath() {
        return String.format("/artifact/%d/%s", this.getId(), this.getFileName());
    }
}
