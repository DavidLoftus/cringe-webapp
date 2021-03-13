package cringe.app.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    Purchase findPurchaseById(int id);

    @Query("SELECT sum(g.price) FROM Purchase p JOIN p.game g WHERE p.game.id = ?1")
    float totalPurchasesByGameId(@Param("gameId") int gameId);

    @Query("SELECT p FROM Purchase p JOIN p.game g WHERE p.game.id = ?1")
    List<Purchase> purchaseByGameID(@Param("gameId") int gameId);
}
