package app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @RequestMapping(value = "/api/ping", method = RequestMethod.GET)
    public void ping() {
    }
}
