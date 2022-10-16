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
    @Autowired
    public UserJpaRepository repo;

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
        return repo.findByName(username);
    }

    public String deleteUserByID(Long id) {
        repo.deleteById(id);
        return "user with id " + id.toString() + " was removed";
    }

    public String deleteUserByName(String name) {
        repo.deleteByName(name);
        return name + " was removed";
    }

    public User updateUser(User usr) {
        User existingUser = repo.findById(usr.getId()).orElse(null);
        existingUser.setUsername(usr.getUsername());
        existingUser.setNameAndSurname(usr.getNameAndSurname());
        existingUser.setEmail(usr.getEmail());
        existingUser.setPassword(usr.getPassword());
        //existingUser.setFavourites(usr.getFavourites());
        //Aquí se pueden añadir futuros atributos
        return repo.save(existingUser);
    }

    public List<Event> getFavouriteEventsByID(Long id) {
        User existingUser = repo.findById(id).orElse(null);
        return existingUser.getFavourites();
    }

    public List<Event> getFavouriteEventsByName(String name) {
        User existingUser = repo.findByName(name);
        return existingUser.getFavourites();
    }


}
