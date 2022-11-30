package cat.cultura.backend.interceptors;

import cat.cultura.backend.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthorizationHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnauthorizedException{
//        if (request.getRequestURI().endsWith("allevents") && request.getHeader("Authorization").contains("eventservice")) return true;
//        if (request.getRequestURI().contains("/documentation") || request.getRequestURI().contains("/swagger") || request.getRequestURI().contains("api-docs")) return true;
//        if (request.getRequestURI().endsWith("/insert")) return true;
//        if ((request.getRequestURI().contains("/events") || request.getRequestURI().contains("/tags")) && request.getMethod().equals("GET")) return true;
//        else if (request.getRequestURI().endsWith("/users") && request.getMethod().equals("POST")) return true;
//        else if (request.getHeader("Authorization") == null) {
//            throw new UnauthorizedException();
//        }
//
        return true;
    }

}
