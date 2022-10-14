package cat.cultura.backend.controller;

import cat.cultura.backend.service.AddFavourite;
import cat.cultura.backend.service.FeatureCommand;
import cat.cultura.backend.service.RemoveFavourite;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FavouritesController {

//    @PutMapping("/user/id={userId}/addToFavourites/event/id={eventId}")
//    public void addToFavourites(@PathVariable Long userId, @PathVariable Long eventId) {
//        service.addToFavourites(userId, eventId);
//    }

    @PutMapping("/user/id={userId}/addToFavourites")
    public String addManyToFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            FeatureCommand fc = new AddFavourite(userId,eventIds);
            fc.execute();
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }

    }

//    @PutMapping("/user/id={userId}/removeFromFavourites/event/id={eventId}")
//    public void removeFromFavourites(@PathVariable Long userId, @PathVariable Long eventId) {
//        service.removeFromFavourites(userId, eventId);
//    }

    @PutMapping("/user/id={userId}/removeManyFromFavourites")
    public String removeFromFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            FeatureCommand fc = new RemoveFavourite(userId,eventIds);
            fc.execute();
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }
}
