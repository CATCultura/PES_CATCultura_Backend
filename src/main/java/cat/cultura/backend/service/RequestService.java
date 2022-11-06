package cat.cultura.backend.service;

import cat.cultura.backend.entity.Request;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.RequestNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.RequestJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.utils.RequestId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RequestService {

    private final UserJpaRepository userRepo;

    private final RequestJpaRepository requestRepo;

    public RequestService(UserJpaRepository userRepo, RequestJpaRepository requestRepo) {
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
    }

    private void addFriendRequestTo(User user, User friend) {
        Request f1 = new Request(user,friend);
        user.addFriendRequestTo(f1);
        friend.addFriendRequestFrom(f1);
        requestRepo.save(f1);
        userRepo.save(user);
        userRepo.save(friend);
    }

    private void removeFriendRequestTo(User user, User friend) {
        Request f1 = new Request(user,friend);
        user.removeFriendRequestTo(f1);
        friend.removeFriendRequestFrom(f1);
        userRepo.save(user);
        userRepo.save(friend);
        requestRepo.delete(f1);
    }

    @Transactional(rollbackFor=Exception.class)
    public void addFriendRequestsTo(Long userId, List<Long> friendIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> friends = userRepo.findAllById(friendIds);
        for(User friend : friends) {
            Request f1 = requestRepo.findById(new RequestId(userId, friend.getId())).orElse(null);
            Request f2 = requestRepo.findById(new RequestId(friend.getId(),userId)).orElse(null);
            if(f1!=null) {
                if(f2!=null) throw new AssertionError("You are already friend with user with id: " + friend.getId() +".\n");
                else throw new AssertionError("You already have a friend request to user with id: " + friend.getId() + ".\n");
            } else if(f2!=null) throw new AssertionError("You have a friend request from user with id: " + friend.getId() + ".\n");
            else addFriendRequestTo(user,friend);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeFriendRequestsTo(Long userId, List<Long> friendIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> friends = userRepo.findAllById(friendIds);
        for(User friend : friends) {
            String message = "Friend request from user " + user.getUsername() +
                    " with id: " + userId + " to user " + friend.getUsername() +
                    " with id: " + friend.getId() + " not found.\n";
            requestRepo.findById(new RequestId(userId, friend.getId())).orElseThrow(() -> new RequestNotFoundException(message));
            Request f2 = requestRepo.findById(new RequestId(friend.getId(), userId)).orElse(null);
            if (f2 != null) {
                throw new AssertionError("The request to user with id: " + friend.getId() + " has already been accepted.\n");
            } else removeFriendRequestTo(user, friend);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void acceptOrDismissFriendRequestsFrom(Long userId,List<Long> requesterIds,boolean accept) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> requesters = userRepo.findAllById(requesterIds);
        for(User requester : requesters) {
            String message = "Friend request from user " + requester.getUsername() +
                    " with id: " + requester.getId() + " to user " + user.getUsername() +
                    " with id: " + userId + " not found.\n";
            requestRepo.findById(new RequestId(requester.getId(), userId)).orElseThrow(() -> new RequestNotFoundException(message));
            Request f2 = requestRepo.findById(new RequestId(userId, requester.getId())).orElse(null);
            if(f2 != null) {
                throw new AssertionError("You are already friend with user with id: " + requester.getId() + ".\n");
            } else if(accept) addFriendRequestTo(user, requester);
            else removeFriendRequestTo(requester, user);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeFriends(Long userId,List<Long> friendIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> friends = userRepo.findAllById(friendIds);
        for(User friend : friends) {
            String message = "Friend request from user " + user.getUsername() +
                    " with id: " + userId + " to user " + friend.getUsername() +
                    " with id: " + friend.getId() + " not found.\n";
            requestRepo.findById(new RequestId(userId, friend.getId())).orElseThrow(() -> new RequestNotFoundException(message));
            String message2 = "Friend request from user " + friend.getUsername() +
                    " with id: " + friend.getId() + " to user " + user.getUsername() +
                    " with id: " + userId + " not found.\n";
            requestRepo.findById(new RequestId(friend.getId(),userId)).orElseThrow(() -> new RequestNotFoundException(message2));
            removeFriendRequestTo(user, friend);
            removeFriendRequestTo(friend, user);
        }
    }

    public List<User> getRequestsTo(Long userId){
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        Set<Request> requestsTo = user.getFriendRequestsFor();
        List<User> users = new ArrayList<>();
        for (Request f: requestsTo) {
            User friend = f.getFriend();
            Request f1 = requestRepo.findById(new RequestId(friend.getId(), userId)).orElse(null);
            if(f1 == null) users.add(friend);
        }
        if(users.isEmpty()) throw new UserNotFoundException("You do not have any request to other users.\n");
        return users;
    }

    public List<User> getRequestFrom(Long userId){
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        Set<Request> requestsFrom = user.getFriendRequestsFrom();
        List<User> users = new ArrayList<>();
        for (Request f: requestsFrom) {
            User requester = f.getRequester();
            Request f1 = requestRepo.findById(new RequestId(userId, requester.getId())).orElse(null);
            if(f1 == null) users.add(requester);
        }
        if(users.isEmpty()) throw new UserNotFoundException("You do not have any request from other users.\n");
        return users;
    }

    @Transactional(rollbackFor=Exception.class)
    public void addFriends(Long userId,List<Long> friendIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> friends = userRepo.findAllById(friendIds);
        for(User friend : friends) {
            Request f1 = requestRepo.findById(new RequestId(friend.getId(), userId)).orElse(null);
            Request f2 = requestRepo.findById(new RequestId(userId, friend.getId())).orElse(null);
            if (f1 != null) {
                if(f2 != null) {
                    throw new AssertionError("You are already friend with user with id: " + friend.getId() + ".\n");
                }
                else addFriendRequestTo(user,friend);
            } else if (f2 != null) {
                addFriendRequestTo(friend,user);
            } else {
                addFriendRequestTo(user,friend);
                addFriendRequestTo(friend,user);
            }
        }
    }

    public List<User> getFriends(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        Set<Request> requestsFor = user.getFriendRequestsFor();
        Set<Request> requestsFrom = user.getFriendRequestsFor();
        List<User> users = new ArrayList<>();
        for (Request f: requestsFor) {
            User friend = f.getFriend();
            Request f1 = requestRepo.findById(new RequestId(friend.getId(), userId)).orElse(null);
            if(f1 != null) users.add(friend);
        }
        if(users.isEmpty()) throw new UserNotFoundException("You do not have any friends.\n");
        return users;
    }

}
