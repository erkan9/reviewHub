package erkamber;


import erkamber.dtos.*;
import erkamber.entities.Role;
import erkamber.requests.CategoryRequestDto;
import erkamber.requests.ReviewRequestDto;
import erkamber.requests.UserRequestDto;
import erkamber.requests.VenueRequestDto;
import erkamber.services.interfaces.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class TestController {

    private final UserService userService;

    private final RoleService roleService;

    private final CategoryService categoryService;

    private final ReviewService reviewService;

    private final VenueService venueService;

    public TestController(UserService userService, RoleService roleService, CategoryService categoryService, ReviewService reviewService, VenueService venueService) {
        this.userService = userService;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
        this.venueService = venueService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<Void> addNewUser(@Valid @RequestBody VenueRequestDto userDto) {

        return null;
    }

    @GetMapping("/users")
    public ResponseEntity<List<CategoryDto>> getUserByUserID() {

        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
