package erkamber.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    @PositiveOrZero
    private int categoryID;

    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 15, message = "Category name must be up to 15 characters")
    private String category;
}
