package cat.cultura.backend.mappers;

import cat.cultura.backend.dtos.ReviewDto;
import cat.cultura.backend.entity.Review;
import cat.cultura.backend.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ModelMapper modelMapper;

    public ReviewMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Review convertReviewDtoToEntity(ReviewDto reviewDto) {
        return modelMapper.map(reviewDto, Review.class);
    }

    public ReviewDto convertReviewToDto(Review review) {
        User author = review.getAuthor();
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);
        reviewDto.setAuthorId(author.getId());
        reviewDto.setAuthorUsername(author.getUsername());
        reviewDto.setEventId(review.getEvent().getId());
        return reviewDto;
    }
}
