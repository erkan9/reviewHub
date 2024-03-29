package erkamber.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @PositiveOrZero
    private int categoryID;

    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 15, message = "Category name must be up to 15 characters")
    private String category;

    private List<VenueDto> venue;
}
