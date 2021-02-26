package cringe.app.security;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        // Added empty cart to user
        Cart c = new Cart();
        user.setCart(c);
        cartRepository.save(c);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // Can add functionality for admins here
        if(roleRepository.findAll().size() == 0) {
            Role role_admin = new Role();
            role_admin.setName("admin");
            role_admin.setUsers(new HashSet<>());
            roleRepository.save(role_admin);

            Role role_user = new Role();
            role_user.setName("user");
            role_user.setUsers(new HashSet<>());
            roleRepository.save(role_user);
        }
        user.setRoles(new HashSet<>(roleRepository.findAllById(Collections.singleton((long) 2))));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
