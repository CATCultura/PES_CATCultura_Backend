package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Review;
import cat.cultura.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    @Query("select m from Review m where " +
            "(m.author = ?1) and (m.blocked = false) ")
    List<Review> findByUser(User user);

    @Query("select m from Review m where " +
            "(m.reports > 0) and (m.blocked = false) ")
    List<Review> findReportedReviews();

    @Query("select m from Review m where " +
            "(m.event = ?1) and (m.blocked = false) ")
    List<Review> findByEvent(Event event);
    @Query("select m from Review m where " +
            "(m.blocked = true) ")
    List<Review> findBlockedReviews();
}
