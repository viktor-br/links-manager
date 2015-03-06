package app;

import java.util.List;
import java.util.UUID;
//import java.util.UUID;

import core.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by viktor on 01.03.15.
 */
public interface ItemRepository extends MongoRepository<Item, String> {
    public List<Item> findByUserId(UUID userId);
    public Item findById(UUID id);
}