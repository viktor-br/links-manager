package app;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:8181")
public class ApiUserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        this.userRepository.deleteAll();
    }

    @Test
    public void registerAndLoginUser() throws Exception {
        String json = this.getUserJson("u1", "u1p");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> login = new HttpEntity<String>(json, httpHeaders);
        restTemplate.put("http://localhost:8181/api/user", login);
        ResponseEntity<Void> loginResults = restTemplate.postForEntity("http://localhost:8181/api/user/login", login, Void.class);
        assertTrue(loginResults.getStatusCode().is2xxSuccessful());
        assertTrue(!loginResults.getHeaders().getFirst("X-AUTH-TOKEN").equals(""));
    }

    @Test
    public void registerUserWithSameUsername() throws Exception {
        String json = this.getUserJson("u1", "u1p");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> login = new HttpEntity<String>(json, httpHeaders);
        restTemplate.put("http://localhost:8181/api/user", login);

        boolean catchedException = false;
        try {
            restTemplate.put("http://localhost:8181/api/user", login);
        } catch (RestClientException ex) {
            catchedException = true;
        }

        assertTrue(catchedException);
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
}