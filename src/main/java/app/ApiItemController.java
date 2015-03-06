package app;

import core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by viktor on 28.02.15.
 */
@RestController
@ComponentScan(basePackages = {"app"})
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

    @RequestMapping(value = "/api/link", method = RequestMethod.PUT, consumes="application/json")
    public Item createLink(@RequestBody Link item) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();
        item.setUserId(currentUser.getUser().getId());
        item.setId(UUID.randomUUID());
        this.repository.save(item);
        return item;
    }

    @RequestMapping(value = "/api/book", method = RequestMethod.PUT, consumes="application/json")
    public Item createBook(@RequestBody Book item) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();
        item.setUserId(currentUser.getUser().getId());
        item.setId(UUID.randomUUID());
        this.repository.save(item);
        return item;
    }

    @RequestMapping(value = "/api/keyphrase", method = RequestMethod.PUT, consumes="application/json")
    public Item createKeyphrase(@RequestBody Keyphrase item) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();
        item.setUserId(currentUser.getUser().getId());
        item.setId(UUID.randomUUID());
        this.repository.save(item);
        return item;
    }

    @RequestMapping(value = "/api/video", method = RequestMethod.PUT, consumes="application/json")
    public Item createVideo(@RequestBody Video item) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getDetails();
        item.setUserId(currentUser.getUser().getId());
        item.setId(UUID.randomUUID());
        this.repository.save(item);
        return item;
    }

    @RequestMapping(value = "/api/item/{id}", method = RequestMethod.DELETE)
    public Item delete(@PathVariable UUID id) throws MalformedURLException {
        // TODO Check if item belongs to the user
        Item item = this.repository.findById(id);
        this.repository.delete(item);
        return item;
    }

    @RequestMapping(value = "/api/link/{id}", method = RequestMethod.POST, consumes="application/json")
    public Item updateLink(@RequestBody Link link) {
        // TODO Check if item belongs to the user
        return link;
    }

    @RequestMapping(value = "/api/book/{id}", method = RequestMethod.POST, consumes="application/json")
    public Item updateBook(@RequestBody Book book) {
        // TODO Check if item belongs to the user
        return book;
    }

    @RequestMapping(value = "/api/keyphrase/{id}", method = RequestMethod.POST, consumes="application/json")
    public Item updateKeyphrase(@RequestBody Keyphrase keyphrase) {
        // TODO Check if item belongs to the user
        return keyphrase;
    }

    @RequestMapping(value = "/api/video/{id}", method = RequestMethod.POST, consumes="application/json")
    public Item updateVideo(@RequestBody Video video) {
        // TODO Check if item belongs to the user
        return video;
    }
}
