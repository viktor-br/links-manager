package app;

import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Item;
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
import java.util.List;

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
        String json = this.getUserJson("u1", "u1p");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> login = new HttpEntity<String>(json, httpHeaders);
        restTemplate.put("http://localhost:8181/api/user", login);
    }

    @Test
    public void createLink() throws Exception {
        String json = this.getLink("http://google.com", "desc", new String[] {"t1", "t2"});

        String token = this.getXAuthToken("u1", "u1p");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-AUTH-TOKEN", token);
        HttpEntity<String> createLink = new HttpEntity<String>(json, httpHeaders);
        restTemplate.put("http://localhost:8181/api/item/link", createLink);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new StringHttpMessageConverter());
//        messageConverters.add(new MappingJacksonHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        HttpEntity<String> getItems = new HttpEntity<String>(json, httpHeaders);
        ResponseEntity searchResults = restTemplate.exchange(new URI("http://localhost:8181/api/item/search"), HttpMethod.POST, getItems, String.class);

//        ResponseEntity<Void> searchResults = restTemplate.postForEntity("http://localhost:8181/api/item/search", "{}", void.class);
        assertTrue(searchResults.getStatusCode().is2xxSuccessful());
//        String res = searchResults.getBody().toString();
//        res.toString();
    }

    /**
     * Return json as a string for user with username and password.
     *
     * @param uname
     * @param upassword
     * @return
     * @throws Exception
     */
    protected String getUserJson(final String uname, final String upassword) throws Exception {
        return new ObjectMapper().writeValueAsString(new Object() {
            private String username = uname;
            private String password = upassword;

            public String getUsername() {
                return username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        });
    }

    /**
     * Login and return X-AUTH-TOKEN
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    protected String getXAuthToken(String username, String password) throws Exception {
        String json = this.getUserJson(username, password);
        // Login
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> login = new HttpEntity<String>(json, httpHeaders);
        ResponseEntity<Void> loginResults = restTemplate.postForEntity("http://localhost:8181/api/user/login", login, Void.class);
        return loginResults.getHeaders().getFirst("X-AUTH-TOKEN");
    }

    protected String getLink(final String iurl, final String idescription, final String[] itags) throws Exception {
        return new ObjectMapper().writeValueAsString(new Object() {
            private String url = iurl;
            private String description = idescription;
            private String[] tags = itags;

            public String getUrl() {
                return url;
            }

            public String getDescription() {
                return description;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setTags(String[] tags) {
                this.tags = tags;
            }

            public String[] getTags() {
                return this.tags;
            }
        });
    }
}