package cat.cultura.backend.service;

import cat.cultura.backend.entity.Request;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.RequestJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RequestService {

    private final UserJpaRepository userRepo;

    private final RequestJpaRepository requestRepo;

    public RequestService(UserJpaRepository userRepo, RequestJpaRepository requestRepo) {
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
    }

    @Transactional(rollbackFor=Exception.class)
    public void addFriendRequestsTo(Long userId, Long friendId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        User friend = userRepo.findById(friendId).orElseThrow(UserNotFoundException::new);
        Request f1 = new Request(user,friend);
        user.addFriendRequestTo(f1);
        friend.addFriendRequestFrom(f1);
        requestRepo.save(f1);
        userRepo.save(user);
        userRepo.save(friend);
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeFriendRequestsTo(Long userId, Long friendId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        User friend = userRepo.findById(friendId).orElseThrow(UserNotFoundException::new);
        Request f1 = new Request(user,friend);
        user.removeFriendRequestTo(f1);
        friend.removeFriendRequestFrom(f1);
        userRepo.save(user);
        userRepo.save(friend);
        requestRepo.delete(f1);
    }

    public List<User> getRequestsTo(Long userId){
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> users = user.getRequestTo();
        if(users.isEmpty()) throw new UserNotFoundException("You do not have any requests to other users.");
        return users;
    }

    public List<User> getRequestFrom(Long userId){
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> users = user.getRequestFrom();
        if(users.isEmpty()) throw new UserNotFoundException("You do not have any requests from other users.");
        return users;
    }

    public List<User> getFriends(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> users = user.getFriends();
        if(users.isEmpty()) throw new UserNotFoundException("You do not have any friends.");
        return users;
    }

}
