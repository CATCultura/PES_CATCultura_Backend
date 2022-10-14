package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class FavouritesService {
    @Autowired
    public UserJpaRepository repoUser;
    @Autowired
    public EventJpaRepository repoEvent;

    /**
     * Adds the event identified by eventId to the favourites list of the user identified by userId, if
     * the user or the event don't exist or the event is already in the favourites list it will throw an Exception
     * @param userId
     * @param eventId
     * @return Ok or HTTP error
     */
    public String addToFavourites(Long userId, Long eventId) {
        User user = repoUser.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Event event = repoEvent.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        try {
            user.addFavourite(event);
        } catch (AssertionError e) {
            return "Event with id: " + eventId + " is already in favourites";
        }
        repoUser.save(user);
        return "Event added successfully";
    }

    public String addManyEventsToFavourites(Long userId, List<Long> eventIds) {
        for(Long eventId : eventIds) {
            if(Objects.equals(addToFavourites(userId, eventId), "Event with id: " + eventId + " is already in favourites")) {
                return "Event with id: " + eventId + " is already in favourites";
            }
        }
        return "Events added successfully";
    }

    public String removeFromFavourites(Long userId, Long eventId) {
        User user = repoUser.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Event event = repoEvent.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        try {
            user.removeFavourite(event);
        } catch (AssertionError e) {
            return "Event with id: " + eventId + " is not in favourites";
        }
        repoUser.save(user);
        return "Event removed successfully";
    }

    public String removeManyEventsFromFavourites(Long userId, List<Long> eventIds) {
        for(Long eventId : eventIds) {
            if(Objects.equals(removeFromFavourites(userId, eventId), "Event with id: " + eventId + " is not in favourites")) {
                return "Event with id: " + eventId + " is not in favourites";
            }
        }
        return "Events removed successfully";
    }


}
