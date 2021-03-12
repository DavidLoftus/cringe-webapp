package cringe.app.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findGameById(int id);

    @Query("SELECT g FROM Game g" +
            " WHERE lower(g.title) LIKE lower(concat('%', ?1,'%'))" +
            " OR lower(g.description) LIKE lower(concat('%', ?1,'%'))")
    List<Game> search(String phrase);

}
