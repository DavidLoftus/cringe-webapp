package cringe.app.security;

import cringe.app.db.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
