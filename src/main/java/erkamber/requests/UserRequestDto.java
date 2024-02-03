package erkamber.requests;

import erkamber.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserRequestDto {

    @PositiveOrZero
    private int userID;

    @NotBlank(message = "Username cannot be Blank")
    @NotEmpty(message = "Username cannot be Empty")
    @NotNull(message = "Username cannot be Null")
    @Size(max = 20, message = "Username must be up to 20 characters long")
    private String userName;

    @NotBlank(message = "Email cannot be Blank")
    @NotEmpty(message = "Email cannot be Empty")
    @NotNull(message = "Email cannot be Null")
    @Size(max = 30, message = "Email must be up to 30 characters long")
    @Email(message = "Email is not Valid")
    private String userEmail;

    @NotBlank(message = "First Name cannot be Blank")
    @NotEmpty(message = "First Name cannot be Empty")
    @NotNull(message = "First Name cannot be Null")
    @Size(max = 20, message = "First Name must be up to 20 characters long")
    private String userFirstName;

    @NotBlank(message = "Last Name cannot be Blank")
    @NotEmpty(message = "Last Name cannot be Empty")
    @NotNull(message = "Last Name cannot be Null")
    @Size(max = 20, message = "Last Name must be up to 20 characters long")
    private String userLastName;

    @NotBlank(message = "Password cannot be Blank")
    @NotEmpty(message = "Password cannot be Empty")
    @NotNull(message = "Password cannot be Null")
    @Size(max = 20, message = "Password must be up to 20 characters long")
    private String userPassword;

    @PositiveOrZero
    int roleID = 1;

    public UserRequestDto(String userName, String userEmail, String userFirstName, String userLastName, String userPassword, int roleID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userPassword = userPassword;
        this.roleID = roleID;
    }
}
