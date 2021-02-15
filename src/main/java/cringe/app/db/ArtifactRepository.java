package cringe.app.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
    Artifact findArtifactById(int id);
}
