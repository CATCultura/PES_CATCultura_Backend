package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AttendedService {

    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    public AttendedService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    public List<Event> removeAttended(Long userId, Long eventId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        user.removeAttended(event);
        userRepo.save(user);
        return user.getAttended();
    }

    public List<Event> confirmAttendance(Long userId, Long eventId, String code) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        if(!Objects.equals(event.getAttendanceCode(), code)) throw new AssertionError("Incorrect Code");
        user.addAttended(event);
        user.addPoints(10);
        userRepo.save(user);
        return user.getAttended();
    }
}