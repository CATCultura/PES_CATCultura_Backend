package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Review;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.ReviewNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
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

    public Review addReview(Long eventId, Review review, Long userId) {
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        review.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        review.setAuthor(user);
        review.setEvent(event);
        return reviewRepo.save(review);
    }

    public List<Review> getReviewsByEvent(Long eventId) {
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        return reviewRepo.findByEvent(event);
    }

    public List<Review> getReviewsByUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return reviewRepo.findByUser(user);
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        reviewRepo.delete(review);
    }

}
