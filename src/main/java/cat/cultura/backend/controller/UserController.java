package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        service.createUser(user);
        return user;
    }

    @PostMapping("/addUsers")
    public List<User> addUser(@RequestBody List<User> users) {
        return service.createUsers(users);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return service.getUsers();
    }

    @GetMapping("/user/id={id}")
    public User findUserById(@PathVariable Long id) {
        return service.getUserByID(id);
    }

    @GetMapping("/user/name={name}")
    public User findUserByName(@PathVariable String name) {
        return service.getUserByUsername(name);
    }

    @GetMapping("/user/id={id}/favourites")
    public List<Event> getFavouritesFromUser(@PathVariable Long id) {
        return service.getFavouriteEventsByID(id);
    }

    @GetMapping("/user/name={name}/favourites")
    public List<Event> getFavouritesFromUser(@PathVariable String name) {
        return service.getFavouriteEventsByName(name);
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User us) {
        return service.updateUser(us);
    }

    @DeleteMapping("/deleteUser/id={id}")
    public String deleteUser(@PathVariable Long id){
        return service.deleteUserByID(id);
    }

    @DeleteMapping("/deleteUser/name={name}")
    public String deleteUser(@PathVariable String name){
        return service.deleteUserByUsername(name);
    }



}
