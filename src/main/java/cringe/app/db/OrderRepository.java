package cringe.app.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(int id);

    List<Order> findOrdersByUser(User user);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    int updateStatus(@Param("orderId") int orderId, @Param("status") Order.Status status);

    @Query("SELECT o FROM Order o LEFT JOIN o.purchases as P LEFT JOIN P.game as g WHERE g.id = :gameId")
    List<Order> findOrdersByGameId(@Param("gameId") int gameId);
}
