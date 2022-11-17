package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.utils.Coordinate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class RouteService {
    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    public RouteService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    public List<Event> getRouteByQuery(double lat, double lon, int radius, String day1, String day2, Long userId){
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        //List<String> favouriteTags = user.getFavouritesTags();
        Coordinate x = new Coordinate(lon,lat);
        Coordinate up = Coordinate.calcEndPoint(x,radius,90);
        Coordinate down = Coordinate.calcEndPoint(x,radius,270);
        Coordinate left = Coordinate.calcEndPoint(x,radius,180);
        Coordinate right = Coordinate.calcEndPoint(x,radius,0);
//        List<Event> events = eventRepo.
//                getEventsByDayAndLocation(day1, day2, up.getLat(), up.getLon(), down.getLat(), down.getLon(),
//                        left.getLat(), left.getLon(), right.getLat(), right.getLon());
//        //events.sortByNumberOfMatches(favouriteTags);
//        List<Event> result = events.subList(0,2);
//        return result;
        return Collections.emptyList();
    }
}
