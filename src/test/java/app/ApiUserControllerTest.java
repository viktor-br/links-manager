package app;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;

import org.hamcrest.Matchers;
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
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:8181")
public class ApiUserControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        this.userRepository.deleteAll();
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void registerUser() throws Exception {
        String json = this.getUserJson("u1", "u1p");
        // TODO maybe it worth to leave same way of request, but mock available to check response code of put request and RestTemplate doesn't
        // maybe there is a way to plug-in authentication in mock.
//        mvc.perform(put("http://localhost:8181/api/user").content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> login = new HttpEntity<String>(json, httpHeaders);
        restTemplate.put("http://localhost:8181/api/user", login);
//        assertTrue(registerResults.getStatusCode().value() == 201);
        ResponseEntity<Void> loginResults = restTemplate.postForEntity("http://localhost:8181/api/user/login", login, Void.class);
        assertTrue(loginResults.getStatusCode().is2xxSuccessful());
        assertTrue(!loginResults.getHeaders().getFirst("X-AUTH-TOKEN").equals(""));

//        mvc.perform(post("http://localhost:8181/api/user/login").content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()).andExpect(header().string("X-AUTH-TOKEN", Matchers.notNullValue()));
    }

    @Test
    public void registerUserWithSameUsername() throws Exception {
        String json = this.getUserJson("u11", "u11p");
        mvc.perform(put("http://localhost:8181/api/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                ;

        mvc.perform(put("http://localhost:8181/api/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
        ;
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