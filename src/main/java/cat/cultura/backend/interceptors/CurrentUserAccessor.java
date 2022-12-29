package cat.cultura.backend.interceptors;

import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserAccessor {

    public String getCurrentUsername(){
        CurrentUser cu = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cu.getUsername();
    }
}
