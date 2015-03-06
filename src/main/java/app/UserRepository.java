package app;

import core.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by viktor on 01.03.15.
 */
public interface UserRepository extends MongoRepository<User, String> {
    public User findByUsername(String username);
}
