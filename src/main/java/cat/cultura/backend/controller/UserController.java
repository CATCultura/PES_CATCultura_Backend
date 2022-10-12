package cat.cultura.backend.controller;

import cat.cultura.backend.entity.User;
import cat.cultura.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/addUser")
    public User addUser(@RequestBody User us) {
        return service.saveUser(us);
    }

    @PostMapping("/addUsers")
    public List<User> addUser(@RequestBody List<User> users) {
        return service.saveUsers(users);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return service.getUsers();
    }

    @GetMapping("/user/{id}")
    public User findUserById(@PathVariable Long id) {
        return service.getUserByID(id);
    }

    @GetMapping("/user/{name}")
    public User findUserByCodi(@PathVariable String name) {
        return service.getUserByName(name);
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User us) {
        return service.updateUser(us);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id){
        return service.deleteUser(id);
    }
}
