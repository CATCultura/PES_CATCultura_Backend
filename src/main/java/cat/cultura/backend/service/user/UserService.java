package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.*;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

@Service
public class UserService {


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserJpaRepository userRepo;

    public UserService(UserJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User createUser(User user) {
        user.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepo.save(user);
        createdUser.createUserHash();
        userRepo.save(createdUser);
        return createdUser;
    }

    public User createOrganizer(Organizer user) {
        user.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        user.setRole(Role.ORGANIZER);
        User createdUser = userRepo.save(user);
        createdUser.createUserHash();
        userRepo.save(createdUser);
        return createdUser;
    }

    public List<User> createUsers(List<User> users) {
        List<User> result = new ArrayList<>();
        for(User user : users) {
            result.add(createUser(user));
        }
        return result;
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with id: " + username + " not found"));
    }

    public Object getUserByUsername(String username, Role r) {
        return userRepo.findByUsernameAndRole(username, r).orElseThrow(() -> new UserNotFoundException("User with id: " + username + " not found"));
    }


    public void deleteUserById(Long id) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        userRepo.delete(existingUser);
    }

    public User updateUser(User usr) {
        return userRepo.save(usr);
    }

    public List<Event> getFavouriteEventsById(Long id) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        return existingUser.getFavourites();
    }

    public List<Event> getAttendanceEventsById(Long id){
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        return existingUser.getAttendance();
    }

    public Page<User> getUsersByQuery(Long id, String username, String nameAndSurname, Pageable pageable) {
        Page<User> result = userRepo.getUsersByQuery(id, username, nameAndSurname, pageable);
        if(result.isEmpty()) throw new UserNotFoundException("No users found");
        else return result;
    }

    public List<Event> getAttendedEventsById(Long id) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
        return existingUser.getAttended();
    }

    public List<Review> getReportsToOtherUsers(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getUpvotedReviews();
    }

    public List<User> report(Long userId, Long reportedUserId) {
        if(userId == reportedUserId) throw new AssertionError("You can´t report yourself");
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        User reportedUser = userRepo.findById(reportedUserId).orElseThrow(UserNotFoundException::new);
        user.addReportToUser(reportedUser);
        reportedUser.report();
        userRepo.save(user);
        userRepo.save(reportedUser);
        return user.getReportedUsers();
    }

    public List<User> quitReport(Long userId, Long reportedUserId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        User reportedUser = userRepo.findById(reportedUserId).orElseThrow(UserNotFoundException::new);
        user.removeReportToUser(reportedUser);
        reportedUser.removeReport();
        userRepo.save(user);
        userRepo.save(reportedUser);
        return user.getReportedUsers();
    }

    public List<User> getMostReportedUsers() {
        List<User> users = userRepo.findReportedUsers();
        users.sort(Comparator.comparingInt(User::getReports));
        return users;
    }

    public List<Event> getOrganizedEvents(Long id) {
        User u = userRepo.findById(id).orElseThrow(UserNotFoundException::new);
        return u.getOrganizedEvents();
    }
}
