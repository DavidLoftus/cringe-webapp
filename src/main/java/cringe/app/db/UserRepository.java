package cringe.app.db;

import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {
    User findByUsername(String username);
}
