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

    public List<Event> getRouteInADayAndLocation(double lat, double lon, int radius, String day, Long userId, List<Long> dEvents) {
        List<Coordinate> c = Coordinate.getQuadrantCoordinates(lon,lat,radius);
        List<Event> events = eventRepo.geByDayAndLocation(day, c.get(0).getLon(), c.get(1).getLon(), c.get(2).getLat(), c.get(3).getLat());
        if(dEvents!=null) events = new ArrayList<>(events.stream().filter(event -> !dEvents.contains(event.getId())).toList());
        int n = events.size();
        List<Event> result = new ArrayList<>();
        if(n > 2) {
            if(userId!=null) {
                User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
                events.sort(new SortByNumberOfMatches(user));
            }
            result = events.subList(0,3);
        }
        else if(n > 1) result = events.subList(0,2);
        else if(n == 1) result = events.subList(0,1);
        return orderEvents(lat,lon,result);
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

    private Event getNearestToMe(double lat, double lon, List<Event> events){
        Event nearestEvent = null;
        double minDist = 100000000;
        for(Event event : events) {
            double dist = distance(lat,lon,event.getLatitud(),event.getLongitud(),"K");
            if (minDist > dist) {
                minDist = dist;
                nearestEvent = event;
            }
        }
        return nearestEvent;
    }

    private List<Event> orderEvents(double lat, double lon, List<Event> events) {
        if (events.isEmpty()) return events;
        else {
            Event ne = getNearestToMe(lat, lon, events);
            List<Event> result = new ArrayList<>(events);
            if (ne != null) {
                result.sort((o1, o2) -> {
                    double d1 = distance(ne.getLatitud(), ne.getLongitud(), o1.getLatitud(), o1.getLongitud(), "K");
                    double d2 = distance(ne.getLatitud(), ne.getLongitud(), o2.getLatitud(), o2.getLongitud(), "K");
                    return Double.compare(d1, d2);
                });
            }
            return result;
        }
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

}
