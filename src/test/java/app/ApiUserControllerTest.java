package app;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import core.User;
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

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:8181")
public class ApiUserControllerTest {

    private String token = "token";

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        this.userRepository.deleteAll();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);

        User admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setToken(this.token);
        admin.setExpires(cal.getTime().getTime());
        this.userRepository.save(admin);
    }

    @Test
    public void registerAndLoginUser() throws Exception {
        Gson gson = new Gson();

        User u = new User();
        u.setUsername("u1");
        u.setPassword("u1p");

        String json = gson.toJson(u);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-AUTH-TOKEN", this.token);
        HttpEntity<String> login = new HttpEntity<String>(json, httpHeaders);
        restTemplate.put("http://localhost:8181/api/user", login);
        ResponseEntity<Void> loginResults = restTemplate.postForEntity("http://localhost:8181/api/user/login", login, Void.class);
        assertTrue(loginResults.getStatusCode().is2xxSuccessful());
        assertTrue(!loginResults.getHeaders().getFirst("X-AUTH-TOKEN").equals(""));
    }

    @Test
    public void registerUserWithSameUsername() throws Exception {
        Gson gson = new Gson();

        User u = new User();
        u.setUsername("u1");
        u.setPassword("u1p");

        String json = gson.toJson(u);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-AUTH-TOKEN", this.token);
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
}