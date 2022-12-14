package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Request;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.FriendAlreadyAddedException;
import cat.cultura.backend.exceptions.RequestAlreadyAddedException;
import cat.cultura.backend.exceptions.RequestNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.RequestJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.utils.RequestId;
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
    public void addFriend(Long userId, Long friendId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        User friend = userRepo.findById(friendId).orElseThrow(UserNotFoundException::new);

        if (user.hasFriend(friend)) throw new FriendAlreadyAddedException();

        Request r = requestRepo.findByRequestId(new RequestId(friendId,userId)).orElse(null);
        if (r == null) {
            if (requestRepo.findByRequestId(new RequestId(userId,friendId)).orElse(null) == null) {
                Request f1 = new Request(user, friend);
                requestRepo.save(f1);
            }
            else throw new RequestAlreadyAddedException();
        }
        else {
            requestRepo.delete(r);
            user.addFriend(friend);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeFriend(Long userId, Long friendId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        User friend = userRepo.findById(friendId).orElseThrow(UserNotFoundException::new);
        if (user.hasFriend(friend)) user.removeFriend(friend);
        else {
            Request f1 = requestRepo.findByRequestId(new RequestId(userId,friendId)).orElseThrow(RequestNotFoundException::new);
            requestRepo.delete(f1);
        }
    }

    public List<User> getRequestsTo(Long userId){
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> users = user.getRequestTo();
        return users;
    }

    public List<User> getRequestFrom(Long userId){
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> users = user.getRequestFrom();
        return users;
    }

    public List<User> getFriends(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> users = user.getFriends();
        return users;
    }

}
