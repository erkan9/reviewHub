package erkamber.dtos;

import erkamber.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    @PositiveOrZero
    private int reviewID;

    @NotBlank(message = "Description is required")
    @Size(max = 400, message = "Description must be at most 400 characters")
    private String description;

    @PositiveOrZero(message = "Rating must be a positive number")
    private int rating;

    @NotNull(message = "Author is required")
    private String authorName;

    @NotNull(message = "Venue is required")
    private String venueName;
}
