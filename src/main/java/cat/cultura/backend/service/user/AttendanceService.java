package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
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

    public List<Event> addAttendance(Long userId, Long eventId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        user.addAttendance(event);
        userRepo.save(user);
        return user.getAttendance();
    }

    public List<Event> removeAttendance(Long userId, Long eventId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        user.removeAttendance(event);
        userRepo.save(user);
        return user.getAttendance();
    }


}

