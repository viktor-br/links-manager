package app;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
@IntegrationTest("server.port:8181")
public class ApiItemControllerTest {

    private MockMvc mvc;

    private String user1Token;

    private String user2Token;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void setUp() throws Exception {
        this.userRepository.deleteAll();
        this.itemRepository.deleteAll();

        // Register test users
        this.user1Token = this.registerUserAndLogin("user1", "pass1");
        this.user2Token = this.registerUserAndLogin("user2", "pass2");
        mvc = MockMvcBuilders.standaloneSetup(new ApiItemController()).build();
    }

    public String registerUserAndLogin(String uname, String upassword) throws Exception {
        mvc.perform(put("http://localhost:8181/api/user").content(this.getUserJson(uname, upassword))
                .contentType(MediaType.APPLICATION_JSON))
        ;

        ResultActions result = mvc.perform(post("http://localhost:8181/api/user").content(this.getUserJson(uname, upassword))
                .contentType(MediaType.APPLICATION_JSON))
        ;

        return result.andReturn().getResponse().getHeader("X-AUTH-TOKEN");
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