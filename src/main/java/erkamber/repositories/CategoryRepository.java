package erkamber.repositories;

import erkamber.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findCategoriesByVenueVenueID(int venueID);

    Optional<Category> findCategoriesByCategory(String category);
}
