package app;

import core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ApiUserController {
    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    public User register(@RequestBody User user) {
        User u = this.repository.findByUsername(user.getUsername());
        if (u == null) {
            user.setId(UUID.randomUUID());
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            this.repository.save(user);
            return user;
        }
        return u;
    }
}
