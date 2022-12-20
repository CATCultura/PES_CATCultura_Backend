package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Route;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.RouteJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRouteService {
    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    private final RouteJpaRepository routeRepo;

    public UserRouteService(EventJpaRepository eventRepo, UserJpaRepository userRepo, RouteJpaRepository routeRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.routeRepo = routeRepo;
    }

    public List<Route> saveRoute(List<Event> routeEvents, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        Route route = new Route();
        route.setRouteEvents(routeEvents);
        Route savedRoute = routeRepo.save(route);
        user.addRoute(route);
        userRepo.save(user);
        return user.getRoutes();
    }

    public List<Route> getRoutes(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        return user.getRoutes();
    }
    public List<Route> deleteRoute(Long routeId, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        Route route = routeRepo.findById(routeId).orElseThrow(() -> new UserNotFoundException("Route with id: " + routeId + "not found"));
        user.deleteRoute(route);
        routeRepo.delete(route);
        return user.getRoutes();
    }
}
