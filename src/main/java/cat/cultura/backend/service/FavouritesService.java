package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> addToFavourites(Long userId, Long eventId) {

        User user = repoUser.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Event event = repoEvent.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        List<Event> favs = user.getFavourites()
                .stream()
                .filter(fav -> eventId.equals(fav.getId())).collect(Collectors.toUnmodifiableList());

        if (favs != null && favs.isEmpty()) {
            user.getFavourites().add(event);
            repoUser.saveAndFlush(user);
            return ResponseEntity.ok("Event added to favorites");
        } else {
            return ResponseEntity //
                    .status(HttpStatus.METHOD_NOT_ALLOWED) //
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                    .body(Problem.create() //
                            .withTitle("Method not allowed") //
                            .withDetail("The event with id " + eventId + " is already in Favourites"));
        }
    }

    public void addManyEventsToFavourites(Long userId, List<Long> eventIds) {
        for(Long eventId : eventIds) {
            addToFavourites(userId,eventId);
        }
    }

    public ResponseEntity<?> removeFromFavourites(Long userId, Long eventId) {

        User user = repoUser.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Event event = repoEvent.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        List<Event> favs = user.getFavourites()
                .stream()
                .filter(fav -> eventId.equals(fav.getId())).collect(Collectors.toUnmodifiableList());

        if (favs != null && !favs.isEmpty()) {
            user.getFavourites().remove(event);
            repoUser.saveAndFlush(user);
            return ResponseEntity.ok("Event removed from favorites");
        } else {
            return ResponseEntity //
                    .status(HttpStatus.METHOD_NOT_ALLOWED) //
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                    .body(Problem.create() //
                            .withTitle("Method not allowed") //
                            .withDetail("The event with id " + eventId + " was not in Favourites"));
        }
    }

    public void removeManyEventsFromFavourites(Long userId, List<Long> eventIds) {
        for(Long eventId : eventIds) {
            removeFromFavourites(userId,eventId);
        }
    }


}
