package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/root")
public class RootController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "root/users";
    }


    @PostMapping("/changeRole")
    @ResponseStatus(value = HttpStatus.OK)
    public void changeRole(@RequestParam int id, @RequestParam String role) {
        User user = userRepository.findById((long) id).get();
        Set<Role> roles = user.getRoles();

        if(user.hasRole(role)) {
            roles.remove(roleRepository.getRoleByName(role));
        } else {
            Role newRole = roleRepository.getRoleByName(role);
            roles.add(newRole);
        }
        user.setRoles(roles);
        for(Role r: user.getRoles())
        {
            System.out.println(r.getName());
        }
        userRepository.save(user);
    }
}
