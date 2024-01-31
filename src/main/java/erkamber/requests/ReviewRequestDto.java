package erkamber.requests;

import erkamber.dtos.UserDto;
import erkamber.dtos.VenueDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @PositiveOrZero
    private int reviewID;

    @NotBlank(message = "Description is required")
    @Size(max = 400, message = "Description must be at most 400 characters")
    private String description;

    @PositiveOrZero(message = "Rating must be a positive number")
    private int rating;

    @PositiveOrZero
    private int authorID;

    @PositiveOrZero
    private int venueID;
}
