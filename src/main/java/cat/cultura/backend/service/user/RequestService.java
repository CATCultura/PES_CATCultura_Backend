package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Request;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.*;
import cat.cultura.backend.interceptors.CurrentUserAccessor;
import cat.cultura.backend.repository.RequestJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.utils.RequestId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RequestService {

    private final UserJpaRepository userRepo;

    private final RequestJpaRepository requestRepo;

    private final CurrentUserAccessor currentUserAccessor;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

        if (user.hasFriend(friend)) {
            logger.warn("User {} ({}) is already friends with user {} ({})",
                    user.getId(), user.getUsername(), friend.getId(), friend.getUsername());
            throw new FriendAlreadyAddedException();
        }

        Request r = requestRepo.findByRequestId(new RequestId(friendId,userId)).orElse(null);
        if (r == null) {
            if (requestRepo.findByRequestId(new RequestId(userId,friendId)).orElse(null) == null) {
                Request f1 = new Request(user, friend);
                requestRepo.save(f1);
            }
            else {
                logger.warn("User {} ({}) has already requested friendship with user {} ({})",
                        user.getId(), user.getUsername(), friend.getId(), friend.getUsername());
                throw new RequestAlreadyAddedException();
            }
        }
        else {
            logger.info("User {} ({}) had already requested friendship with user {} ({}). Adding friendship",
                    friend.getId(), friend.getUsername(), user.getId(), user.getUsername());
            requestRepo.delete(r);
            user.addFriend(friend);
        }
        return user.getFriends();
    }

    @Transactional(rollbackFor=Exception.class)
    public List<User> removeFriend(Long userId, Long friendId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        User friend = userRepo.findById(friendId).orElseThrow(UserNotFoundException::new);
        if (!user.getUsername().equals(currentUserAccessor.getCurrentUsername())) {
            logger.warn("User {} is trying to remove user {} ({}) friendships",
                    currentUserAccessor.getCurrentUsername(), user.getId(), user.getUsername());
            throw new ForbiddenActionException();
        }
        if (user.hasFriend(friend)) {
            logger.info("Removing {} from {} friendships",
                    user.getUsername(), friend.getUsername());
            user.removeFriend(friend);
        }
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
