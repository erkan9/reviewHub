package erkamber.services.interfaces;

import erkamber.dtos.ReviewDto;
import erkamber.requests.ReviewRequestDto;

import java.util.List;

public interface ReviewService {

    List<ReviewDto> findReviewsByAuthorID(int authorID);

    List<ReviewDto> findReviewsByVenueID(int venueID);

    ReviewDto saveReview(ReviewRequestDto reviewDto);

    ReviewDto findReviewByAuthorIdAndVenueID(int authorID, int venueID);

    void deleteReview(int reviewID);
}