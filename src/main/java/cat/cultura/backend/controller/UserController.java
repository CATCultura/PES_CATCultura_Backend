package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        service.createUser(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return service.getUsers();
    }

    @GetMapping("/users?id={id}")
    public User findUserById(@PathVariable Long id) {
        return service.getUserByID(id);
    }

    @GetMapping("/users?name={name}")
    public User findUserByName(@PathVariable String name) {
        return service.getUserByUsername(name);
    }

    @GetMapping("/users/{id}/favourites")
    public List<Event> getFavouritesFromUser(@PathVariable Long id) {
        return service.getFavouriteEventsByID(id);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User us) {
        return service.updateUser(us);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        return service.deleteUserByID(id);
    }

    @PutMapping("/users/{userId}/favourites")
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

    @DeleteMapping("/users/{userId}/favourites")
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

    @PutMapping("/users/{userId}/trophy")
    public String addManyToTrophies(@PathVariable Long userId, @RequestBody List<Long> trophiesIds) {
        try {
            FeatureCommand fc = new AddTrophy(userId,trophiesIds);
            fc.execute();
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }

    @DeleteMapping("/users/{userId}/trophy")
    public String removeFromTrophy(@PathVariable Long userId, @RequestBody List<Long> trophiesIds) {
        try {
            FeatureCommand fc = new RemoveTrophy(userId,trophiesIds);
            fc.execute();
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }

}
