package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CurrentUser implements UserDetails {
    private core.User user;

    @Autowired
    private UserRepository repository;

    @NotNull
    private boolean accountExpired;

    @NotNull
    private boolean accountLocked;

    @NotNull
    private boolean credentialsExpired;

    @NotNull
    private boolean accountEnabled;

    private String username;

    private String password;

    private long expires;

    private String token;

    public CurrentUser() {
    }

    public CurrentUser(String username) {
        // TODO user exists check is required
        this.user = this.repository.findByUsername(username);
        this.username = username;
    }

    public CurrentUser(core.User user) {
        this.user = user;
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    @JsonIgnore
    public core.User getUser() {
        return user;
    }

    public void setUser(core.User user) {
        this.user = user;
    }

    @JsonIgnore
    public void getId() {
    }

    @Override
    @JsonIgnore
    public Set<UserAuthority> getAuthorities() {
        return new HashSet<UserAuthority>();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return !accountEnabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getToken() {
        return token;
    }

    @JsonIgnore
    public long getExpires() {
        return expires;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
