package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.ReviewDto;
import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.Review;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.mappers.ReviewMapper;
import cat.cultura.backend.mappers.UserMapper;
import cat.cultura.backend.service.user.ReviewService;
import cat.cultura.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ModerationController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("reviews/{reviewId}/block")
    public ResponseEntity<ReviewDto> block(@PathVariable Long reviewId) {
            Review review;
            try {
                review = reviewService.blockReview(reviewId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(reviewMapper.convertReviewToDto(review));
    }

    @DeleteMapping("reviews/{reviewId}/block")
    public ResponseEntity<ReviewDto> unBlock(@PathVariable Long reviewId) {
            Review review;
            try {
                review = reviewService.unblockReview(reviewId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(reviewMapper.convertReviewToDto(review));
    }

    @GetMapping("reviews/reported")
    public ResponseEntity<List<ReviewDto>> getMostReportedReviews() {
        List<Review> reviews;
        try {
            reviews = reviewService.getMostReportedReviews();
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(reviewMapper::convertReviewToDto).toList());
    }

    @GetMapping("reviews/blocked")
    public ResponseEntity<List<ReviewDto>> getBlockedReviews() {
        List<Review> reviews;
        try {
            reviews = reviewService.getBlockedReviews();
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(reviewMapper::convertReviewToDto).toList());
    }

    @GetMapping("users/reported")
    public ResponseEntity<List<UserDto>> getMostReportedUsers() {
        List<User> users;
        try {
            users = userService.getMostReportedUsers();
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(userMapper::convertUserToDto).toList());
    }

}
