package erkamber.services.interfaces;

import erkamber.dtos.CategoryDto;
import erkamber.requests.CategoryRequestDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryDto saveCategory(CategoryRequestDto categoryDto);

    CategoryDto findCategoriesByVenueID(int venueID);

    CategoryDto findCategoryByCategory(String category);

    List<CategoryDto> getAllCategories();
}
