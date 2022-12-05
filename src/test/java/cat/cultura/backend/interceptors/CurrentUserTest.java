package cat.cultura.backend.interceptors;

import cat.cultura.backend.entity.Role;
import cat.cultura.backend.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {

    private class GrantedAuthorityImpl implements GrantedAuthority {

        private final Role r;

        public GrantedAuthorityImpl(Role r) {
            this.r = r;
        }
        @Override
        public String getAuthority() {
            return r.toString();
        }
    }

    @Test
    void getAuthoritiesForUser() {
        User u = new User("Joan");
        u.setRole(Role.USER);
        CurrentUser cu = new CurrentUser(u);

        List<GrantedAuthority> expected = new ArrayList<>();
        expected.add(new GrantedAuthorityImpl(Role.USER));
        List<String> expectedRoles = new ArrayList<>();
        for (GrantedAuthority g : expected) {
            expectedRoles.add(g.getAuthority());
        }

        List<GrantedAuthority> result = (List<GrantedAuthority>) cu.getAuthorities();
        List<String> resultRoles = new ArrayList<>();
        for (GrantedAuthority g : result) {
            resultRoles.add(g.getAuthority());
        }
        Assertions.assertEquals(expectedRoles,resultRoles);
    }

    @Test
    void getAuthoritiesForOrganizer() {
        User u = new User("Joan");
        u.setRole(Role.ORGANIZER);
        CurrentUser cu = new CurrentUser(u);

        List<GrantedAuthority> expected = new ArrayList<>();
        expected.add(new GrantedAuthorityImpl(Role.USER));
        expected.add(new GrantedAuthorityImpl(Role.ORGANIZER));
        List<String> expectedRoles = new ArrayList<>();
        for (GrantedAuthority g : expected) {
            expectedRoles.add(g.getAuthority());
        }

        List<GrantedAuthority> result = (List<GrantedAuthority>) cu.getAuthorities();
        List<String> resultRoles = new ArrayList<>();
        for (GrantedAuthority g : result) {
            resultRoles.add(g.getAuthority());
        }
        Assertions.assertEquals(expectedRoles,resultRoles);
    }

    @Test
    void getAuthoritiesForAdmin() {
        User u = new User("Joan");
        u.setRole(Role.ADMIN);
        CurrentUser cu = new CurrentUser(u);

        List<GrantedAuthority> expected = new ArrayList<>();
        expected.add(new GrantedAuthorityImpl(Role.USER));
        expected.add(new GrantedAuthorityImpl(Role.ADMIN));
        expected.add(new GrantedAuthorityImpl(Role.ORGANIZER));
        expected.add(new GrantedAuthorityImpl(Role.SERVICE));
        List<String> expectedRoles = new ArrayList<>();
        for (GrantedAuthority g : expected) {
            expectedRoles.add(g.getAuthority());
        }

        List<GrantedAuthority> result = (List<GrantedAuthority>) cu.getAuthorities();
        List<String> resultRoles = new ArrayList<>();
        for (GrantedAuthority g : result) {
            resultRoles.add(g.getAuthority());
        }
        Assertions.assertEquals(expectedRoles,resultRoles);
    }

    @Test
    void getAuthoritiesForService() {
        User u = new User("Joan");
        u.setRole(Role.SERVICE);
        CurrentUser cu = new CurrentUser(u);

        List<GrantedAuthority> expected = new ArrayList<>();
        expected.add(new GrantedAuthorityImpl(Role.USER));
        expected.add(new GrantedAuthorityImpl(Role.SERVICE));
        List<String> expectedRoles = new ArrayList<>();
        for (GrantedAuthority g : expected) {
            expectedRoles.add(g.getAuthority());
        }

        List<GrantedAuthority> result = (List<GrantedAuthority>) cu.getAuthorities();
        List<String> resultRoles = new ArrayList<>();
        for (GrantedAuthority g : result) {
            resultRoles.add(g.getAuthority());
        }
        Assertions.assertEquals(expectedRoles,resultRoles);
    }
}