package cat.cultura.backend.controller;

import cat.cultura.backend.service.AddFavouriteCommand;
import cat.cultura.backend.service.FavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FavouritesController {
    @Autowired
    private FavouritesService service;


    @PutMapping("user/id={userId}/addToFavourites/event/id={eventId}")
    public ResponseEntity<?> addToFavourites(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.addToFavourites(userId, eventId);
    }

    @PutMapping("/user/id={userId}/addManyToFavourites/")
    public String addManyToFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            service.addManyEventsToFavourites(userId, eventIds);
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }



    }

    @PutMapping("user/id={userId}/removeFromFavourites/event/id={eventId}")
    public ResponseEntity<?> removeFromFavourites(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.addToFavourites(userId, eventId);
    }

    @PutMapping("user/id/{userId}/removeManyFromFavourites/")
    public void removeFromFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        service.addManyEventsToFavourites(userId, eventIds);
    }
}
