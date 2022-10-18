package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class UserService {

    private final UserJpaRepository repo;

    public UserService(UserJpaRepository repo) {
        this.repo = repo;
    }

    public User createUser(User user) {
        user.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return repo.save(user);
    }

    public List<User> createUsers(List<User> users) {
        for(User user : users) {
            user.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        }
        return repo.saveAll(users);
    }

    public List<User> getUsers() {
        return repo.findAll();
    }

    public User getUserByID(Long id) {
        return repo.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return repo.findByUsername(username);
    }

    public String deleteUserByID(Long id) {
        repo.deleteById(id);
        return "user with id " + id.toString() + " was removed";
    }

    public String deleteUserByUsername(String username) {
        repo.deleteByUsername(username);
        return username + " was removed";
    }

    public User updateUser(User usr) {
        return repo.save(usr);
    }

    public List<Event> getFavouriteEventsByID(Long id) {
        User existingUser = repo.findById(id).orElse(null);
        return existingUser.getFavourites();
    }

    public List<Event> getFavouriteEventsByName(String name) {
        User existingUser = repo.findByUsername(name);
        return existingUser.getFavourites();
    }


}
