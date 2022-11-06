package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class FavouriteService {

    private final EventJpaRepository eventRepo;

    private final UserJpaRepository userRepo;

    public FavouriteService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    @Transactional(rollbackFor=Exception.class)
    public void addFavourite(Long userId, List<Long> eventIds) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found\n"));
        List<Event> events = eventRepo.findAllById(eventIds);
        for (Event e : events) {
            user.addFavourite(e);
        }
        userRepo.save(user);
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeFavourite(Long userId, List<Long> eventIds) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "not found\n"));
        List<Event> events = eventRepo.findAllById(eventIds);
        for (Event e : events) {
            user.removeFavourite(e);
        }
        userRepo.save(user);
    }

}
