package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private UserTrophyService userTrophyService;

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        userService.createUser(user);
        return user;
    }


    @GetMapping("/users")
    public List<User> findUsers(@RequestParam(name = "name", required = false) String name) {

        if (name == null) return userService.getUsers();
        List<User> u = new ArrayList<>();
        u.add(userService.getUserByUsername(name));
        return u;

    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Long id) {
        return userService.getUserByID(id);
    }



    @GetMapping("/users/{id}/favourites")
    public List<Event> getFavouritesFromUser(@PathVariable Long id) {
        return userService.getFavouriteEventsByID(id);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User us) {
        return userService.updateUser(us);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        return userService.deleteUserByID(id);
    }

    @PutMapping("/users/{userId}/favourites")
    public String addManyToFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            favouriteService.addFavourite(userId,eventIds);
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }

    @DeleteMapping("/users/{userId}/favourites")
    public String removeFromFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            favouriteService.removeFavourite(userId, eventIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

    @PutMapping("/users/{userId}/trophy")
    public String addManyToTrophies(@PathVariable Long userId, @RequestBody List<Long> trophiesIds) {
        try {
            userTrophyService.addTrophy(userId,trophiesIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

    @DeleteMapping("/users/{userId}/trophy")
    public String removeFromTrophy(@PathVariable Long userId, @RequestBody List<Long> trophiesIds) {
        try {
            userTrophyService.removeTrophy(userId,trophiesIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

}
