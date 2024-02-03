package erkamber.repositories;

import erkamber.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {

    Optional<Venue> findVenueByVenueName(String venueName);

    List<Venue> findVenueByVenueCity(String city);

    List<Venue> findVenueByCategoryCategoryID(int categoryID);

    List<Venue> findVenueByVenueCityOrderByAverageRatingAsc(String city);

    List<Venue> findVenueByVenueCityOrderByAverageRatingDesc(String city);

    @Query("SELECT v FROM Venue v WHERE v.venueCity = :venueCity AND v.category.category = :category")
    List<Venue> findVenueByVenueCityAndCategoryCategory(@Param("venueCity") String venueCity, @Param("category") String category);

    Optional<Venue> findVenueByPhoneNumber(String phoneNumber);

    Optional<Venue> findVenueByEmail(String email);

    List<Venue> findAllByOrderByAverageRatingAsc();

    List<Venue> findAllByOrderByAverageRatingDesc();
}
