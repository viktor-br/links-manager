package app;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class ApiUserControllerTest {
//
//    @Autowired
//    private UserRepository repository;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new ApiUserController()).build();
    }

    @Test
    public void registerUser() throws Exception {
        core.User user = new core.User();
        user.setUsername("ut1");
        user.setPassword("ut1p");
        String json = new ObjectMapper().writeValueAsString(new Object() {
            private String username = "u1";
            private String password = "u1p";

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
        mvc.perform(put("/api/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(content().json("username"))
//                .andExpect(content().json("password"))
        ;
    }
}