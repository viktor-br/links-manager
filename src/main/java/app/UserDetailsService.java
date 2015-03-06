package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by viktor on 04.03.15.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        core.User user = userRepository.findByUsername(username);
        if(user != null) {
            return new CurrentUser(user);
        } else {
            throw new UsernameNotFoundException("could not find the user '"
                    + username + "'");
        }
    }
}
