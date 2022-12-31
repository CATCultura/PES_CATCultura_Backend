package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Request;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.*;
import cat.cultura.backend.interceptors.CurrentUserAccessor;
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

    private CurrentUserAccessor currentUserAccessor;

    public RequestService(UserJpaRepository userRepo,
                          RequestJpaRepository requestRepo,
                          CurrentUserAccessor currentUserAccessor) {
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
        this.currentUserAccessor = currentUserAccessor;
    }

    @Transactional(rollbackFor=Exception.class)
    public List<User> addFriend(Long userId, Long friendId) {
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
        return user.getFriends();
    }

    @Transactional(rollbackFor=Exception.class)
    public List<User> removeFriend(Long userId, Long friendId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        User friend = userRepo.findById(friendId).orElseThrow(UserNotFoundException::new);
        if (!user.getUsername().equals(currentUserAccessor.getCurrentUsername())) throw new ForbiddenActionException();
        if (user.hasFriend(friend)) user.removeFriend(friend);
        else {
            Request f1 = requestRepo.findByRequestId(new RequestId(userId,friendId)).orElse(null);
            if (f1 == null) {
                f1 = requestRepo.findByRequestId(new RequestId(friendId,userId)).orElseThrow(RequestNotFoundException::new);
            }
            requestRepo.delete(f1);
        }
        return user.getFriends();
    }

    public List<User> getRequestsTo(Long userId){
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getRequestTo();
    }

    public List<User> getRequestFrom(Long userId){
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getRequestFrom();
    }

    public List<User> getFriends(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getFriends();
    }

}
