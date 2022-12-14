package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavouriteService {

    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    public FavouriteService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    @Transactional(rollbackFor=Exception.class)
    public void addFavourite(Long userId, Long eventId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + "not found"));
        user.addFavourite(event);
        userRepo.save(user);
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeFavourite(Long userId, Long eventId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found"));
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + "not found"));
        user.removeFavourite(event);
        userRepo.save(user);
    }

}
