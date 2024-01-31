package erkamber.services.implementations;

import erkamber.dtos.CategoryDto;
import erkamber.entities.Category;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.repositories.CategoryRepository;
import erkamber.requests.CategoryRequestDto;
import erkamber.services.interfaces.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto saveCategory(CategoryRequestDto categoryDto) {

        Category category = modelMapper.map(categoryDto, Category.class);

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto findCategoriesByVenueID(int venueID) {

        Category category = categoryRepository.findCategoriesByVenueID(venueID);

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto findCategoryByCategory(String category) {

        Optional<Category> optionalCategory = categoryRepository.findCategoriesByCategory(category);

        Category searchedCategory = optionalCategory.orElseThrow(() ->
                new ResourceNotFoundException("Category not Found", "Category"));

        return modelMapper.map(searchedCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        List<Category> allCategories = categoryRepository.findAll();

        return mapToDtoList(allCategories);
    }

    private List<CategoryDto> mapToDtoList(List<Category> categories) {

        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    Category getCategoryOfVenue(int venueID) {

        return categoryRepository.findCategoriesByVenueID(venueID);
    }

    Category categoryByCategoryID(int categoryID) {

        Optional<Category> optionalCategory = categoryRepository.findById(categoryID);

        return optionalCategory.orElseThrow(() ->
                new ResourceNotFoundException("Category not Found", "Category"));
    }

}
