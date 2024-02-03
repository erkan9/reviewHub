package erkamber.dtos;

import erkamber.entities.Category;
import erkamber.entities.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDto {

    @PositiveOrZero
    private int venueID;

    @NotBlank(message = "Venue name is required")
    @Size(max = 40, message = "Venue name must be at most 40 characters")
    private String venueName;

    @NotBlank(message = "Venue description is required")
    @Size(max = 300, message = "Venue description must be at most 300 characters")
    private String venueDescription;

    @NotBlank(message = "Venue city is required")
    @Size(max = 50, message = "Venue city must be at most 50 characters")
    private String venueCity;

    @NotBlank(message = "Venue address is required")
    @Size(max = 100, message = "Venue address must be at most 100 characters")
    private String venueAddress;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 20, message = "Email must be at most 20 characters")
    private String email;

    @Min(value = 0, message = "Average rating cannot be negative")
    @DecimalMax(value = "5.00", message = "Average rating cannot be greater than 5.00")
    private double averageRating = 0;

    int reviewCount = 0;

    private String category;

    List<Review> venueReviews;
}
