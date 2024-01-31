package erkamber.repositories;

import erkamber.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.author.userID = :authorID")
    List<Review> findReviewsByAuthorID(@Param("authorID") int authorID);

    @Query("SELECT r FROM Review r WHERE r.venue.venueID = :venueID")
    List<Review> findReviewsByVenueID(@Param("venueID") int venueID);

    @Query("SELECT r FROM Review r WHERE r.author.userID = :authorID AND r.venue.venueID = :venueID")
    Optional<Review> findReviewsByAuthorIDAndVenueID(@Param("authorID") int authorID, @Param("venueID") int venueID);

}