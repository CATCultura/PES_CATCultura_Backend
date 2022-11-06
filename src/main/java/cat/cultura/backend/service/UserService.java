package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class UserService {

    private final UserJpaRepository userRepo;

    public UserService(UserJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User createUser(User user) {
        user.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return userRepo.save(user);
    }

    public List<User> createUsers(List<User> users) {
        for(User user : users) {
            user.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        }
        return userRepo.saveAll(users);
    }

    public User getUserByID(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found\n"));
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with id: " + username + " not found\n"));
    }

    public void deleteUserByID(Long id) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found\n"));
        userRepo.delete(existingUser);
    }

    public User updateUser(User usr) {
        return userRepo.save(usr);
    }

    public List<Event> getFavouriteEventsByID(Long id) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found\n"));
        return existingUser.getFavourites();
    }

    public List<Event> getAttendanceEventsByID(Long id){
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found\n"));
        return existingUser.getAttendance();
    }

    public Page<User> getUsersByQuery(Long id, String username, String nameAndSurname, Pageable pageable) {
        Page<User> result = userRepo.getUsersByQuery(id, username, nameAndSurname, pageable);
        if(result.isEmpty()) throw new UserNotFoundException("No users found\n");
        else return result;
    }

}
