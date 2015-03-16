package app;

import core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@RestController
public class ApiUserController {
    @Autowired
    private UserRepository repository;

    /**
     * Return 201 Created if ok and 403 Forbidden.
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/api/user", method = RequestMethod.PUT, consumes="application/json")
    public ResponseEntity<String> register(@RequestBody User user) throws Exception {
        User u = this.repository.findByUsername(user.getUsername());
        if (u != null) {
            return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
        }
        user.setId(UUID.randomUUID());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        this.repository.save(user);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }
}
