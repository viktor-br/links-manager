package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.*;
import core.item.Item;
import core.item.ItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.net.MalformedURLException;

@RestController
public class ApiItemController {
    @Autowired
    private ItemRepository repository;

    @RequestMapping(value = "/api/item/{id}", method = RequestMethod.GET)
    public Item getItem(@PathVariable UUID id) throws MalformedURLException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();

        return this.repository.findById(id);
    }

    @RequestMapping(value = "/api/item/search", method = RequestMethod.POST, consumes="application/json")
    public List<Item> getItems(@RequestBody SearchFilter sf) throws MalformedURLException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();

        return this.repository.findByUserId(currentUser.getUser().getId());
    }

    @RequestMapping(value = "/api/item/{type}", method = RequestMethod.PUT, consumes="application/json")
    public Item createItem(@PathVariable String type, @RequestBody String json) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();
        ObjectMapper objectMapper = new ObjectMapper();
        Item item;
        try {
            Class<Item> cl = ItemFactory.factory(type);
            item = objectMapper.readValue(json, cl);
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't parse json into a search request", e);
        }
        item.setUserId(currentUser.getUser().getId());
        item.setId(UUID.randomUUID());
        this.repository.save(item);
        return item;
    }

    @RequestMapping(value = "/api/item/{id}", method = RequestMethod.DELETE)
    public Item delete(@PathVariable UUID id) throws MalformedURLException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();
        Item item = this.repository.findById(id);
        if (item.getUserId().equals(currentUser.getUser().getId())) {
            this.repository.delete(item);
        } else {
            // TODO request other user item
        }
        return item;
    }

    @RequestMapping(value = "/api/item/{id}", method = RequestMethod.POST, consumes="application/json")
    public Item updateItem(@PathVariable UUID id, @RequestBody String json) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();
        Item item = this.repository.findById(id);
        if (item == null) {
            // TODO throw exception about item absence
        }
        if (item.getUserId().equals(currentUser.getUser().getId())) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Item newItem = objectMapper.readValue(json, item.getClass());
                item.populate(objectMapper.readValue(json, item.getClass()));
            } catch (IOException e) {
                throw new IllegalArgumentException("Couldn't parse json into an item request", e);
            }
            this.repository.save(item);
        }

        return item;
    }
}
