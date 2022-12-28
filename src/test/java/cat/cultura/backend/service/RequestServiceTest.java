package cat.cultura.backend.service;

import cat.cultura.backend.entity.Request;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.RequestAlreadyAddedException;
import cat.cultura.backend.repository.RequestJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.service.user.RequestService;
import cat.cultura.backend.utils.RequestId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class RequestServiceTest {

    @Autowired
    private RequestService requestService;

    @MockBean
    private RequestJpaRepository requestRepo;

    @MockBean
    private UserJpaRepository userRepo;

    @Test
    void addingFriendOneTimeWorks() {
        User requester = new User("Joan");
        requester.setId(1L);

        Optional<User> req = Optional.of(requester);

        User friend = new User("Ernest");
        friend.setId(2L);
        Optional<User> fri = Optional.of(friend);

        given(userRepo.findById(1L)).willReturn(req);
        given(userRepo.findById(2L)).willReturn(fri);

        given(requestRepo.findByRequestId(new RequestId(requester.getId(), friend.getId()))).willReturn(Optional.empty());
        given(requestRepo.findByRequestId(new RequestId(friend.getId(), requester.getId()))).willReturn(Optional.empty());

        Request expected = new Request(requester,friend);

        given(requestRepo.save(expected)).willReturn(expected);
        requestService.addFriend(1L,2L);

        Assertions.assertTrue(true);
    }

    @Test
    void addingFriendTwoTimesDoesntWork() {
        User requester = new User("Joan");
        requester.setId(1L);

        Optional<User> req = Optional.of(requester);

        User friend = new User("Ernest");
        friend.setId(2L);
        Optional<User> fri = Optional.of(friend);

        given(userRepo.findById(1L)).willReturn(req);
        given(userRepo.findById(2L)).willReturn(fri);

        Request expected = new Request(requester,friend);
        given(requestRepo.findByRequestId(new RequestId(requester.getId(), friend.getId()))).willReturn(Optional.of(expected));

        given(requestRepo.save(expected)).willReturn(expected);

        Assertions.assertThrows(RequestAlreadyAddedException.class, () -> requestService.addFriend(1L,2L));
    }

    @Test
    void addingInverseRequestAddsFriend() {
        User requester = new User("Joan");
        requester.setId(1L);

        Optional<User> req = Optional.of(requester);

        User friend = new User("Ernest");
        friend.setId(2L);
        Optional<User> fri = Optional.of(friend);

        given(userRepo.findById(1L)).willReturn(req);
        given(userRepo.findById(2L)).willReturn(fri);

        Request expected = new Request(friend,requester);
        given(requestRepo.findByRequestId(new RequestId(requester.getId(), friend.getId()))).willReturn(Optional.empty());
        given(requestRepo.findByRequestId(new RequestId(friend.getId(), requester.getId()))).willReturn(Optional.of(expected));

        given(requestRepo.save(expected)).willReturn(expected);

        requestService.addFriend(1L,2L);

        Assertions.assertTrue(requester.getFriends().contains(friend));
    }

    @Test
    void removingFriendOk() {
        User requester = new User("Joan");
        requester.setId(1L);

        Optional<User> req = Optional.of(requester);

        User friend = new User("Ernest");
        friend.setId(2L);

        Optional<User> fri = Optional.of(friend);
        requester.addFriend(friend);

        given(userRepo.findById(1L)).willReturn(req);
        given(userRepo.findById(2L)).willReturn(fri);

        requestService.removeFriend(1L,2L);

        Assertions.assertFalse(requester.getFriends().contains(friend));
    }


}