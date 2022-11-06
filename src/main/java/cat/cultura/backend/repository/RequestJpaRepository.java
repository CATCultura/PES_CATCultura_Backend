package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Request;
import cat.cultura.backend.utils.RequestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestJpaRepository extends JpaRepository<Request, RequestId> {

//    @Query("select m from friend_consents m where " +
//            "(m.friendId = ?1) " +
//            "and not exists " +
//            "(select n from friend_consents n where " +
//            "(n.userId = ?1) and (n.friendId = m.userId))")
//    Page<User> getFriendRequestsToUser(Long id, final Pageable pageable);
//
//    @Query("select m from friend_consents m where " +
//            "(m.userId = ?1) " +
//            "and not exists " +
//            "(select n from friend_consents n where " +
//            "(n.userId = m.friendId) and (n.userId = ?1))")
//    Page<User> getFriendRequestsFromUser(Long id, final Pageable pageable);
//
//    @Query("select m from friend_consents m where " +
//            "(m.friendId = ?1) " +
//            "and exists " +
//            "(select n from friend_consents n where " +
//            "(n.userId = ?1) and (n.friendId = m.userId))")
//    Page<User> getFriends(Long id, final Pageable pageable);
//      @Query("select m from Friendship m where " +
//              "(m.friend1 = ?1) or (m.friend2 = ?1)" )
//      List<FriendRequest> getFriends(Long id);
//
//      @Query("select m from Friendship m where " +
//              "(m.friend1 = ?1 and m.friend2 = ?2)" +
//              "or (m.friend1 = ?2 and m.friend2 = ?1)")
//      Optional<FriendRequest> getFriendship(Long id, Long id2);

}
