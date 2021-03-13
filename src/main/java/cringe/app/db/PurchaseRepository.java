package cringe.app.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    Purchase findPurchaseById(int id);

    // TODO(evanSpendlove)
    // Find total purchases by game

    // @Query("SELECT sum(g.price) FROM Cart c JOIN c.games g WHERE c.id = ?1")
    @Query("SELECT sum(g.price) FROM Purchase p JOIN p.game g WHERE p.game.id = ?1")
    int totalPurchasesByGameId(@Param("gameId") int gameId);

    // Find total purchases by games
}
