package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    @PositiveOrZero
    private int roleID;

    @NotBlank(message = "Role cannot be Blank")
    @NotEmpty(message = "Role cannot be Empty")
    @NotNull(message = "Role cannot be Null")
    @Size(max = 30, message = "Role must be up to 30 characters long")
    private String role;
}
