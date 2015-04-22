package app;

import java.util.List;
import java.util.UUID;
//import java.util.UUID;

import core.item.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {
    public List<Item> findByUserId(UUID userId);
    public Item findById(UUID id);
}
