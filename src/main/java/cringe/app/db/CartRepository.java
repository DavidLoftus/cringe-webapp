package cringe.app.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT sum(g.price) FROM Cart c JOIN c.games g WHERE c.id = ?1")
    Float getTotalCost(int cartId);

}