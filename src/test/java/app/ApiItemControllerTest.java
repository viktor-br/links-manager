package app;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class ApiItemControllerTest {

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new ApiItemController()).build();
    }

    @Test
    public void registerUser() throws Exception {
        core.User user = new core.User();
        user.setId(UUID.randomUUID());
        user.setUsername("ut1");
        user.setPassword("ut1p");
        MockHttpServletRequestBuilder s = MockMvcRequestBuilders.put("/api/user").accept(MediaType.APPLICATION_JSON);
        mvc.perform(s)
                .andExpect(status().isOk())
                .andExpect(content().json("username"))
                .andExpect(content().json("password"))
        ;
    }
}