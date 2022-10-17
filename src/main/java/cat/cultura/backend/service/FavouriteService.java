package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FavouriteService {

    private final EventJpaRepository eventRepo;
    private final UserJpaRepository userRepo;

    public FavouriteService(EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    public void addFavourite(Long userID, List<Long> eventIDs) {
        User user = userRepo.findById(userID).orElseThrow(UserNotFoundException::new);
        List<Event> events = eventRepo.findAllById(eventIDs);
        for (Event e : events) {
            user.addFavourite(e);
        }
        userRepo.save(user);
    }

    public void removeFavourite(Long userID, List<Long> eventIDs) {
        User user = userRepo.findById(userID).orElseThrow(UserNotFoundException::new);
        List<Event> events = eventRepo.findAllById(eventIDs);
        for (Event e : events) {
            user.removeFavourite(e);
        }
        userRepo.save(user);
    }
}
