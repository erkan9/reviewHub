package erkamber.services.implementations;

import erkamber.dtos.ReviewDto;
import erkamber.entities.Review;
import erkamber.entities.User;
import erkamber.entities.Venue;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.repositories.ReviewRepository;
import erkamber.requests.ReviewRequestDto;
import erkamber.services.interfaces.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ModelMapper modelMapper;

    private final VenueServiceImpl venueService;

    private final UserServiceImpl userService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ModelMapper modelMapper, VenueServiceImpl venueService, UserServiceImpl userService) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.venueService = venueService;
        this.userService = userService;
    }

    @Override
    public List<ReviewDto> findReviewsByAuthorID(int authorID) {

        List<Review> reviewsOfVenue = reviewRepository.findReviewsByAuthorID(authorID);

        return mapListToReviewDto(reviewsOfVenue);
    }

    @Override
    public List<ReviewDto> findReviewsByVenueID(int venueID) {

        List<Review> reviewsOfVenue = reviewRepository.findReviewsByVenueID(venueID);

        return mapListToReviewDto(reviewsOfVenue);
    }

    @Override
    public ReviewDto saveReview(ReviewRequestDto reviewDto) {

        Review review = modelMapper.map(reviewDto, Review.class);

        int rating = review.getRating();

        User author = userService.findUserByID(reviewDto.getAuthorID());

        review.setAuthor(author);

        reviewRepository.save(review);

        Venue venue = venueService.findVenueByID(reviewDto.getVenueID());

        venueService.updateVenueRating(rating, venue.getVenueID());

        return modelMapper.map(reviewDto, ReviewDto.class);
    }

    @Override
    public ReviewDto findReviewByAuthorIdAndVenueID(int authorID, int venueID) {

        Optional<Review> reviewOptional = reviewRepository.findReviewsByAuthorIDAndVenueID(authorID, venueID);

        Review review = reviewOptional.orElseThrow(() ->
                new ResourceNotFoundException("Review not Found", "Review"));

        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public void deleteReview(int reviewID) {

        Optional<Review> reviewOptional = reviewRepository.findById(reviewID);

        Review review = reviewOptional.orElseThrow(() ->
                new ResourceNotFoundException("Review not Found", "Review"));

        Venue venue = venueService.findVenueByID(review.getVenue().getVenueID());

        int rating = review.getRating();

        venueService.updateVenueRatingOnDelete(rating, venue.getVenueID());

        reviewRepository.delete(review);
    }

    private List<ReviewDto> mapListToReviewDto(List<Review> listOfReviews) {

        return listOfReviews.stream()
                .map(reviews -> modelMapper.map(reviews, ReviewDto.class))
                .collect(Collectors.toList());
    }
}
