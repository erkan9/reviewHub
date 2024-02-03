package erkamber.services.interfaces;

import erkamber.dtos.UserDto;
import erkamber.requests.UserRequestDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserRequestDto userDto);

    UserDto findByID(int userID);

    UserDto findByUserName(String userName);

    UserDto findByEmail(String userEmail);

    List<UserDto> findByFirstName(String firstName);

    List<UserDto> findByLastName(String lastName);

    List<UserDto> getAllUsers();

    UserDto login(String username, String password);

    int deleteByID(int userID);

    int deleteByUsername(String username);

    List<UserDto> findUsersByRoleID(int roleID);

    void updateUser(UserDto userDto);
}
