package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AttendedService {

    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    public AttendedService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    public void removeAttended(Long userId, Long eventId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + "not found"));
        user.removeAttended(event);
        userRepo.save(user);
    }

    public void confirmAttendance(Long userId, Long eventId, String code) {

        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + "not found"));
        if(!Objects.equals(event.getAttendanceCode(), code)) throw new AssertionError("Incorrect Code");
        user.addAttended(event);
        user.addPoints(10);
        userRepo.save(user);
    }
}