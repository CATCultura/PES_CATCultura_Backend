package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.utils.Coordinate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteDataService {
    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    public RouteDataService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
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
        return eventRepo.getEventsByDayAndLocation(day, up.getLon(), down.getLon(), left.getLat(), right.getLat());
    }

    class SortByNumberOfMatches implements Comparator<Event> {
        private final List<Tag> ambits;
        private final List<Tag> categ;
        private final List<Tag> altresCateg;

        public SortByNumberOfMatches(User user) {
            this.ambits = user.getTagsAmbits();
            this.categ = user.getTagsCateg();
            this.altresCateg = user.getTagsAltresCateg();
        }

        public int compare(Event a, Event b) {
            int n1 = 0;
            int n2 = 0;
            for(Tag tag : ambits){
                if(a.getTagsAmbits().contains(tag)) ++n1;
                if(b.getTagsAmbits().contains(tag)) ++n2;
            }
            for(Tag tag : categ){
                if(a.getTagsCateg().contains(tag)) ++n1;
                if(b.getTagsCateg().contains(tag)) ++n2;
            }
            for(Tag tag : altresCateg){
                if(a.getTagsAltresCateg().contains(tag)) ++n1;
                if(b.getTagsAltresCateg().contains(tag)) ++n2;
            }
            return Integer.compare(n1, n2);
        }
    }

    class SortByLongitude implements Comparator<Event> {
        public int compare(Event a, Event b) {
            return Double.compare(a.getLongitud(), b.getLongitud());
        }
    }

}
