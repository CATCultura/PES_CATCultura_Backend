package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    private final UserJpaRepository userRepo;

    public FriendService(UserJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void addFriends(Long userID, List<Long> friendsIDs) {
        User user = userRepo.findById(userID).orElseThrow(UserNotFoundException::new);
        List<User> friends = userRepo.findAllById(friendsIDs);
        for (User friend : friends) {
            user.addFriend(friend);
        }
        userRepo.save(user);
    }

    public void removeFriends(Long userID, List<Long> friendsIDs) {
        User user = userRepo.findById(userID).orElseThrow(UserNotFoundException::new);
        List<User> friends = userRepo.findAllById(friendsIDs);
        for (User friend : friends) {
            user.removeFriend(friend);
        }
        userRepo.save(user);
    }
}
