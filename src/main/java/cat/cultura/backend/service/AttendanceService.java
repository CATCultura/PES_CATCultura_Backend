package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    public AttendanceService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    public void addAttendance(Long userId, Long eventId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + "not found"));
        user.addAttendance(event);
        userRepo.save(user);
    }

    public void removeAttendance(Long userId, Long eventId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + "not found"));
        user.removeAttendance(event);
        userRepo.save(user);
    }

}

