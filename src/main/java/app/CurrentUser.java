package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Date;

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

    public CurrentUser() {
    }

    public CurrentUser(String username) {
        // TODO user exists check is required
        this.user = this.repository.findByUsername(username);
        this.username = username;
    }

    public CurrentUser(String username, Date expires) {
        // TODO user exists check is required
        this.user = this.repository.findByUsername(username);
        this.username = username;
        this.expires = expires.getTime();
    }

    public CurrentUser(core.User user) {
        this.user = user;
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    private long expires;

    public core.User getUser() {
        return user;
    }

    @JsonIgnore
    public void getId() {
    }

    @Override
    @JsonIgnore
    public Set<UserAuthority> getAuthorities() {
        return new HashSet<UserAuthority>();
    }

    @JsonIgnore
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

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
