package cringe.app.db;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(int id);
    List<Order> findOrdersByUser(User user);
}
