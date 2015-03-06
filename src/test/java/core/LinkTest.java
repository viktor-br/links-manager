package core;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by viktor on 28.02.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class LinkTest {
    @Test
    public void addTags() {
        Link l = new Link();
        l.setDescription("test");
        l.addTag(new String("java"));
        l.addTag(new String("golang"));
        l.addTag(new String("java"));

        assertTrue("description getter/setter wrong operation", l.getDescription().equals("test"));
        assertTrue("addTag() works improperly ", l.getTags().size() == 2);

        l.removeTagByName(new String("java"));
        l.removeTagByName(new String("golang"));

        assertTrue("removeTagByName works improperly ", l.getTags().size() == 0);
    }
}
