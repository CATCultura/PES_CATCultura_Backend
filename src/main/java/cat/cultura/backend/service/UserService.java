package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserJpaRepository repo;

    public User saveUser(User usr) {
        return repo.save(usr);
    }

    public List<User> saveUsers(List<User> users) {
        return repo.saveAll(users);
    }

    public List<User> getUsers() {
        return repo.findAll();
    }

    public User getUserByID(Long id) {
        return repo.findById(id).orElse(null);
    }

    public User getUserByName(String name) {
        return repo.findByName(name);
    }

    public String deleteUser(Long id) {
        repo.deleteById(id);
        return "user removed";
    }

    public User updateUser(User usr) {
        User existingUser =repo.findById(usr.getId()).orElse(null);
        existingUser.setName(usr.getName());
        existingUser.setFavourites(usr.getFavourites());
        return repo.save(existingUser);
    }
}
