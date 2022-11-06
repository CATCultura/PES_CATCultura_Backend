package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AttendanceService {

    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    public AttendanceService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    public void addAttendance(Long userId, List<Long> eventIds) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found\n"));
        List<Event> events = eventRepo.findAllById(eventIds);
        for (Event e : events) {
            user.addAttendance(e);
        }
        userRepo.save(user);
    }

    public void removeAttendance(Long userId, List<Long> eventIds) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found\n"));
        List<Event> events = eventRepo.findAllById(eventIds);
        for (Event e : events) {
            user.removeAttendance(e);
        }
        userRepo.save(user);
    }

}

