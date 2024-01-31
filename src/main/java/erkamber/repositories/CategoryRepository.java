package erkamber.repositories;

import erkamber.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT v.category FROM Venue v WHERE v.venueID = :venueID")
    Category findCategoriesByVenueID(@Param("venueID") int venueID);

    Optional<Category> findCategoriesByCategory(String category);
}
