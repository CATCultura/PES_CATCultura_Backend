package cat.cultura.backend.interceptors;

import cat.cultura.backend.entity.Role;
import cat.cultura.backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CurrentUser implements UserDetails {

    private List<String> extractAuthorities(Role r) {
        List<String> a = new ArrayList<>();
        switch(r) {
            case USER -> {
             a.add("USER");
            }
            case ADMIN -> {
                a.add("USER");
                a.add("ADMIN");
                a.add("ORGANIZER");
                a.add("SERVICE");
            }
            case SERVICE -> {
                a.add("USER");
                a.add("SERVICE");
            }
            case ORGANIZER -> {
                a.add("USER");
                a.add("ORGANIZER");
            }
        }
        return a;
    }
    private final User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<>();
        List<String> authorities = extractAuthorities(user.getRole());
        for (String s : authorities) {
            auth.add((GrantedAuthority) () -> s);
        }
        return auth;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public CurrentUser(User user) {
        this.user = user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public User getUser() {
        return this.user;
    }

}
