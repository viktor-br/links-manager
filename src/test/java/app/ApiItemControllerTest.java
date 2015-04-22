package app;

import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import core.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.net.URL;

import core.parser.ItemJsonParser;
import core.item.Link;
import core.item.Item;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:8181")
public class ApiItemControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void setUp() throws Exception {
        this.userRepository.deleteAll();
        this.itemRepository.deleteAll();

        // Create two test user
        User u = new User();
        u.setUsername("u1");
        u.setPassword("u1p");
        Gson gson = new Gson();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> login = new HttpEntity<String>(gson.toJson(u), httpHeaders);
        restTemplate.put("http://localhost:8181/api/user", login);
    }

    @Test
    public void createLink() throws Exception {
        Gson gson = new Gson();

        User u = new User();
        u.setUsername("u1");
        u.setPassword("u1p");

        String linkUrl = "http://google.com";
        String linkDesc = "desc";
        String[] linkTags = new String[] {"t1", "t2"};
        Link link = new Link();
        link.setUrl(new URL(linkUrl));
        link.setDescription(linkDesc);
        HashSet tags = new HashSet<String>();
        for(int i = 0; i < linkTags.length; i++) {
            tags.add(linkTags[i]);
        }
        link.setTags(tags);

        String json = gson.toJson(link);

        String token = this.getXAuthToken(u);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-AUTH-TOKEN", token);

        restTemplate.put("http://localhost:8181/api/item/link", new HttpEntity<String>(json, httpHeaders));
        restTemplate.put("http://localhost:8181/api/item/link", new HttpEntity<String>(json, httpHeaders));

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new StringHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        HttpEntity<String> getItems = new HttpEntity<String>(json, httpHeaders);
        ResponseEntity searchResults = restTemplate.exchange(new URI("http://localhost:8181/api/item/search"), HttpMethod.POST, getItems, String.class);

        assertTrue(searchResults.getStatusCode().is2xxSuccessful());

        ItemJsonParser parser = new ItemJsonParser();
        ArrayList<Item> items = parser.parseArray(searchResults.getBody().toString());

        assertTrue(items.size() == 2);
        assertTrue(items.get(0) instanceof Link);
        assertTrue(items.get(0).getUrl().equals(new URL(linkUrl)));
        assertTrue(items.get(0).getDescription().equals(linkDesc));
        assertTrue(items.get(0).getTags().size() == linkTags.length);
    }

    /**
     * Login and return X-AUTH-TOKEN
     *
     * @param u
     * @return
     * @throws Exception
     */
    protected String getXAuthToken(User u) throws Exception {
        // Login
        Gson gson = new Gson();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> login = new HttpEntity<String>(gson.toJson(u), httpHeaders);
        ResponseEntity<Void> loginResults = restTemplate.postForEntity("http://localhost:8181/api/user/login", login, Void.class);
        return loginResults.getHeaders().getFirst("X-AUTH-TOKEN");
    }
}