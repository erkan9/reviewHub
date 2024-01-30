package erkamber.services.interfaces;

import erkamber.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

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
}
