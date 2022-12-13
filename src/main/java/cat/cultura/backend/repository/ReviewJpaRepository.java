package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    @Query("select m from Review m where " +
            "(m.author = ?1) ")
    List<Review> findByUser(Long userId);

    @Query("select m from Review m where " +
            "(m.author = ?1) ")
    List<Review> findByEvent(Long eventId);
}
