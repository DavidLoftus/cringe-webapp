package cringe.app.security;

import cringe.app.config.CringeConfig;
import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;

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

    @Autowired
    private CringeConfig config;

    @PostConstruct
    private void init() {
        // Can add functionality for admins here
        if(roleRepository.findAll().size() == 0) {
            Role role_admin = new Role();
            role_admin.setName("admin");
            role_admin.setUsers(Collections.emptySet());
            roleRepository.save(role_admin);

            Role role_user = new Role();
            role_user.setName("user");
            role_user.setUsers(Collections.emptySet());
            roleRepository.save(role_user);

            Role root_role = new Role();
             root_role.setName("root");
             root_role.setUsers(Collections.emptySet());
            roleRepository.save( root_role);
        }

        if (config.isRootEnabled()) {
            User rootUser = userRepository.findByUsername(config.getRootUsername());
            if (rootUser == null) {
                User newUser = new User();
                newUser.setUsername(config.getRootUsername());
                newUser.setPassword(config.getRootPassword());
                save(newUser);

                rootUser = userRepository.findByUsername(config.getRootUsername());
            }

            if (!rootUser.hasRole("admin")) {
                rootUser.getRoles().add(roleRepository.getRoleByName("admin"));
                rootUser.getRoles().add(roleRepository.getRoleByName("root"));
                userRepository.save(rootUser);
            }
        }

    }

    private Set<Role> defaultRoles() {
        return Set.of(roleRepository.getRoleByName("user"));
    }

    @Override
    public void save(User user) {
        // Added empty cart to user
        Cart c = new Cart();
        user.setCart(c);
        cartRepository.save(c);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(defaultRoles());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
