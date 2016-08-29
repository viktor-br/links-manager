package app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

    private final TokenHandler tokenHandler;

    @Autowired
    private UserRepository repository;

    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") String secret) {
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
    }

    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final CurrentUser user = authentication.getDetails();
        user.getUser().setExpires(System.currentTimeMillis() + TEN_DAYS);
        String token = tokenHandler.createTokenForUser(user);
        user.getUser().setToken(token);
        this.repository.save(user.getUser());
        response.addHeader(AUTH_HEADER_NAME, token);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            User u = this.repository.findByToken(token);
            final CurrentUser user = tokenHandler.parseUserFromToken(u);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }
}
