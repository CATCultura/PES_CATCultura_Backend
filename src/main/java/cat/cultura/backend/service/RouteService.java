package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Route;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.RouteJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.utils.Coordinate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteService {
    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    private final RouteJpaRepository routeRepo;

    public RouteService(EventJpaRepository eventRepo, UserJpaRepository userRepo, RouteJpaRepository routeRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.routeRepo = routeRepo;
    }

    public Route saveRoute(List<Event> routeEvents, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        Route route = new Route();
        route.setRouteEvents(routeEvents);
        Route savedRoute = routeRepo.save(route);
        user.addRoute(route);
        userRepo.save(user);
        return savedRoute;
    }

    public List<Route> getRoutes(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        return user.getRoutes();
    }

    public List<Event> getRouteInADayAndLocation(double lat, double lon, int radius, String day) {
        List<Event> events = getRouteByQuery(lat, lon, radius, day);
        List<Event> result = new ArrayList<>();
        int n = events.size();
        if(n > 2) {
            result = events.subList(0,3);
            result.sort(new SortByLongitude());
        }
        else if(n > 1) result = events.subList(0,2);
        else if(n == 1) result = events.subList(0,1);

        return result;
    }

    public List<Event> getUserAdjustedRouteInADayAndLocation(double lat, double lon, int radius, String day, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        List<Event> events = getRouteByQuery(lat, lon, radius, day);
        List<Event> result = new ArrayList<>();
        int n = events.size();
        if(n > 2) {
            events.sort(new SortByNumberOfMatches(user));
            result = events.subList(0,3);
            result.sort(new SortByLongitude());
        }
        else if(n > 1) result = events.subList(0,2);
        else if(n == 1) result = events.subList(0,1);

        return result;
    }

    private List<Event> getRouteByQuery(double lat, double lon, int radius, String day) {
        Coordinate x = new Coordinate(lon,lat);
        Coordinate up = Coordinate.calcEndPoint(x,radius,90);
        Coordinate down = Coordinate.calcEndPoint(x,radius,270);
        Coordinate left = Coordinate.calcEndPoint(x,radius,180);
        Coordinate right = Coordinate.calcEndPoint(x,radius,0);
        List<Event> events = eventRepo.getEventsByDayAndLocation(day, up.getLon(), down.getLon(), left.getLat(), right.getLat());
        return events;
    }

    public void deleteRoute(Long routeId, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        Route route = routeRepo.findById(routeId).orElseThrow(() -> new UserNotFoundException("Route with id: " + routeId + "not found"));
        user.deleteRoute(route);
        routeRepo.delete(route);
    }

    class SortByNumberOfMatches implements Comparator<Event> {
        private List<Tag> ambits;
        private List<Tag> categ;
        private List<Tag> altresCateg;

        public SortByNumberOfMatches(User user) {
            this.ambits = user.getTagsAmbits();
            this.categ = user.getTagsCateg();
            this.altresCateg = user.getTagsAltresCateg();
        }

        private void sumPresence(int n1,int n2, List<Tag> tags1, List<Tag> tags2, List<Tag> tags) {
            for(Tag tag : tags){
                  if(tags1.contains(tag)) ++n1;
                  if(tags2.contains(tag)) ++n2;
            }
        }

        public int compare(Event a, Event b) {
            int n1 = 0;
            int n2 = 0;
            sumPresence(n1,n2,a.getTagsAmbits(),b.getTagsAmbits(),ambits);
            sumPresence(n1,n2,a.getTagsCateg(),b.getTagsCateg(), categ);
            sumPresence(n1,n2,a.getTagsAltresCateg(),b.getTagsAltresCateg(), altresCateg);
            if(n1-n2 >= 0) return 1;
            else return -1;
        }
    }

    class SortByLongitude implements Comparator<Event> {
        public int compare(Event a, Event b) {
            double x = a.getLongitud() - b.getLongitud();
            if(x > 0) return 1;
            else if(x < 0) return -1;
            else return 0;
        }
    }

}
