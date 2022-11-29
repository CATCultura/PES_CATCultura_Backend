package cat.cultura.backend.interceptors;

import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UnauthorizedException;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;

import static java.lang.Integer.parseInt;

@Service
public class AuthorizationHandlerInterceptor implements HandlerInterceptor {

    private UserJpaRepository userJpaRepository;
    @Autowired
    public AuthorizationHandlerInterceptor(UserJpaRepository userJpaRepository){
        this.userJpaRepository= userJpaRepository;

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnauthorizedException{
        if (request.getRequestURI().endsWith("allevents") && request.getHeader("Authorization").contains("eventservice")) return true;
        if (request.getRequestURI().contains("/documentation") || request.getRequestURI().contains("/swagger") || request.getRequestURI().contains("api-docs")) return true;
        if (request.getRequestURI().endsWith("/insert")) return true;
        if ((request.getRequestURI().contains("/events") || request.getRequestURI().contains("/tags")) && request.getMethod().equals("GET")) return true;
        else if (request.getRequestURI().endsWith("/users") && request.getMethod().equals("POST")) return true;
        else if (request.getHeader("Authorization") == null) {
            throw new UnauthorizedException();
        }
//        SecurityContext sc = SecurityContextHolder.createEmptyContext();
//        String api = request.getHeader("Authorization");
//        User u = userJpaRepository.findByUserHash(api);
//        sc.setAuthentication(new Authentication() {
//            @Override
//            public Collection<? extends GrantedAuthority> getAuthorities() {
//                return null;
//            }
//
//            @Override
//            public Object getCredentials() {
//                return null;
//            }
//
//            @Override
//            public Object getDetails() {
//                return null;
//            }
//
//            @Override
//            public Object getPrincipal() {
//                return u;
//            }
//
//            @Override
//            public boolean isAuthenticated() {
//                return true;
//            }
//
//            @Override
//            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//
//            }
//
//            @Override
//            public String getName() {
//                return null;
//            }
//        });

        return true;
    }

}
