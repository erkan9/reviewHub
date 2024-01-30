package erkamber.repositories;

import erkamber.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {

    Optional<Venue> findVenueByVenueName(String venueName);

    List<Venue> findVenueByVenueCity(String city);

    List<Venue> findVenueByVenueCityAndAverageRating(String city, double averageRating);

    List<Venue> findVenueByVenueCityAndCategoryCategory(String venueCity, String category);

    Optional<Venue> findVenueByPhoneNumber(String phoneNumber);

    Optional<Venue> findVenueByEmail(String email);
}
