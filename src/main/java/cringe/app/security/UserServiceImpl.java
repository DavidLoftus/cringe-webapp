package cringe.app.security;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        user.setRoles(new HashSet<>(roleRepository.findAll())); // Change appropriately for regular users and admins
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
