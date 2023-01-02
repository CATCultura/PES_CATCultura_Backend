package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Route;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.RouteJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class UserRouteService {

    private final UserJpaRepository userRepo;

    private final EventJpaRepository eventRepo;

    private final RouteJpaRepository routeRepo;

    public UserRouteService(UserJpaRepository userRepo, RouteJpaRepository routeRepo, EventJpaRepository eventRepo) {
        this.userRepo = userRepo;
        this.routeRepo = routeRepo;
        this.eventRepo = eventRepo;
    }

    public List<Route> saveRoute(Route route, Long userId, List<Long> eventIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        route.setCreatedAt(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        List<Event> events = new ArrayList<>();
        for(Long eventId : eventIds) {
            Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
            events.add(event);
        }
        route.addEvents(events);
        routeRepo.save(route);
        user.addRoute(route);
        userRepo.save(user);
        return user.getRoutes();
    }

    public List<Route> getRoutes(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getRoutes();
    }
    public List<Route> deleteRoute(Long routeId, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        Route route = routeRepo.findById(routeId).orElseThrow(UserNotFoundException::new);
        user.deleteRoute(route);
        routeRepo.delete(route);
        return user.getRoutes();
    }
}
