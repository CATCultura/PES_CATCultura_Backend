package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Review;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.ReviewNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.ReviewJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
@Service
public class ReviewService {

    private final ReviewJpaRepository reviewRepo;
    private final UserJpaRepository userRepo;
    private final EventJpaRepository eventRepo;
    
    public ReviewService(ReviewJpaRepository reviewRepo, EventJpaRepository eventRepo, UserJpaRepository userRepo) {
        this.eventRepo = eventRepo;
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
    }

    public Review addReview(Long eventId, Review review, User user) {
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        review.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        List<User> u = userRepo.findAll();
        User user2 = u.get(0);
        review.setAuthor(user2);
        review.setEvent(event);
        return reviewRepo.save(review);
    }

    public List<Review> getReviewsByEvent(Long eventId) {
        return reviewRepo.findByEvent(eventId);
    }

    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepo.findByUser(userId);
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        reviewRepo.delete(review);
    }

}
