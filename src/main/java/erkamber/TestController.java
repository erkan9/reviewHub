package erkamber;


import erkamber.dtos.UserDto;
import erkamber.services.interfaces.RoleService;
import erkamber.services.interfaces.UserService;
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

    public TestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<Void> addNewUser(@Valid @RequestBody UserDto userDto) {

        UserDto user = userService.addUser(userDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUserByUserID() {

        return ResponseEntity.ok(userService.findUsersByRoleID(1));
    }
}
