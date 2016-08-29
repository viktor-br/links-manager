package app;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.User;

public final class TokenHandler {
    private static final String HMAC_ALGO = "HmacSHA256";
    private final Mac hmac;

    public TokenHandler(byte[] secretKey) {
        try {
            hmac = Mac.getInstance(HMAC_ALGO);
            hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
        }
    }

    public CurrentUser parseUserFromToken(User user) {
        CurrentUser currentUser = new CurrentUser(user);
        if (user.getExpires() > new Date().getTime()) {
            return currentUser;
        }
        return null;
    }

    public String createTokenForUser(CurrentUser user) {
        byte[] userBytes = toJSON(user);
        byte[] hash = createHmac(userBytes);
        final StringBuilder sb = new StringBuilder(170);
        sb.append(toBase64(hash));
        String h = sb.toString();

        user.getUser().setToken(h);
        return h;
    }

    private byte[] toJSON(CurrentUser user) {
        try {
            CurrentUserAuth cua = new CurrentUserAuth();
            cua.setId(user.getUser().getId());
            cua.setUsername(user.getUsername());
            cua.setExpires(user.getUser().getExpires());
            return new ObjectMapper().writeValueAsBytes(cua);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String toBase64(byte[] content) {
        return DatatypeConverter.printBase64Binary(content);
    }

    // synchronized to guard internal hmac object
    private synchronized byte[] createHmac(byte[] content) {
        return hmac.doFinal(content);
    }
}
