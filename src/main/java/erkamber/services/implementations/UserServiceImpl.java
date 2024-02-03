package erkamber.services.implementations;

import erkamber.configurations.PasswordEncoderConfiguration;
import erkamber.dtos.UserDto;
import erkamber.entities.Role;
import erkamber.entities.User;
import erkamber.exceptions.InvalidInputException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.repositories.UserRepository;
import erkamber.requests.UserRequestDto;
import erkamber.services.interfaces.UserService;
import erkamber.validations.UserValidation;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final UserValidation userValidation;

    private final PasswordEncoderConfiguration passwordEncoderConfiguration;

    private final RoleServiceImpl roleService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           UserValidation userValidation, PasswordEncoderConfiguration passwordEncoderConfiguration, RoleServiceImpl roleService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userValidation = userValidation;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
        this.roleService = roleService;
    }

    @Override
    public UserDto addUser(UserRequestDto userDto) {

        isUserNameValid(userDto.getUserName());

        isUserFirstOrLastNameValid(userDto.getUserFirstName());

        isUserFirstOrLastNameValid(userDto.getUserLastName());

        String encodedUserPassword = passwordEncoderConfiguration.passwordEncoder().encode(userDto.getUserPassword());

        userDto.setUserPassword(encodedUserPassword);

        User newUser = modelMapper.map(userDto, User.class);

        Role role = roleService.getRoleByID(userDto.getRoleID());

        newUser.setRole(role);

        userRepository.save(newUser);

        return modelMapper.map(newUser, UserDto.class);
    }

    @Override
    public UserDto findByID(int userID) {

        Optional<User> searchedUserOptional = userRepository.findById(userID);

        User searchedUser = searchedUserOptional.orElseThrow(() ->
                new ResourceNotFoundException("User ID not Found:" + userID, "User"));

        getUserRole(searchedUser.getUserID(), searchedUser);

        return modelMapper.map(searchedUser, UserDto.class);
    }

    private void getUserRole(int userID, User searchedUser) {

        // Role role = roleService.getRoleByUserID(userID);

        // searchedUser.setRole(role);
    }

    @Override
    public UserDto findByUserName(String userName) {

        Optional<User> searchedUserOptional = userRepository.findUserByUserName(userName);

        User searchedUser = searchedUserOptional.orElseThrow(() ->
                new ResourceNotFoundException("User not found:" + userName, "User"));

        getUserRole(searchedUser.getUserID(), searchedUser);

        return modelMapper.map(searchedUser, UserDto.class);
    }

    @Override
    public UserDto findByEmail(String userEmail) {

        Optional<User> searchedUserOptional = userRepository.findByUserEmail(userEmail);

        User searchedUser = searchedUserOptional.orElseThrow(() ->
                new ResourceNotFoundException("User not found:" + userEmail, "User"));

        getUserRole(searchedUser.getUserID(), searchedUser);

        return modelMapper.map(searchedUser, UserDto.class);
    }

    @Override
    public List<UserDto> findByFirstName(String firstName) {

        List<User> searchedUser = userRepository.findUserByUserFirstName(firstName);

        isUserListEmpty(searchedUser);

        searchedUser.forEach(user -> getUserRole(user.getUserID(), user));

        return mapListToUserDto(searchedUser);
    }

    @Override
    public List<UserDto> findByLastName(String lastName) {

        List<User> searchedUser = userRepository.findUserByUserLastName(lastName);

        isUserListEmpty(searchedUser);

        searchedUser.forEach(user -> getUserRole(user.getUserID(), user));

        return mapListToUserDto(searchedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> allUsers = userRepository.findAll();

        allUsers.forEach(user -> getUserRole(user.getUserID(), user));

        return mapListToUserDto(allUsers);
    }

    @Override
    public List<UserDto> findUsersByRoleID(int roleID) {

        List<User> usersByRoleID = userRepository.findUsersByRoleID(roleID);

        usersByRoleID.forEach(user -> getUserRole(user.getUserID(), user));

        return mapListToUserDto(usersByRoleID);
    }

    @Override
    public void updateUser(UserDto userDto) {

        Optional<User> searchedUserOptional = userRepository.findById(userDto.getUserID());

        User searchedUser = searchedUserOptional.orElseThrow(() ->
                new ResourceNotFoundException("User not found:" + userDto.getUserID(), "User"));

        if (StringUtils.isNotBlank(userDto.getUserFirstName())) {
            searchedUser.setUserFirstName(userDto.getUserFirstName().trim());
        }

        // Validate and update last name
        if (StringUtils.isNotBlank(userDto.getUserLastName())) {
            searchedUser.setUserLastName(userDto.getUserLastName().trim());
        }

        // Validate and update role
        if (userDto.getRole() != null && StringUtils.isNotBlank(userDto.getRole().getRole())) {
            searchedUser.setRole(modelMapper.map(userDto.getRole(), Role.class));
        }

        userRepository.save(modelMapper.map(userDto, User.class));
    }

    @Override
    public UserDto login(String username, String password) {

        User loginUser = getLoginUser(username, password);

        getUserRole(loginUser.getUserID(), loginUser);

        return modelMapper.map(loginUser, UserDto.class);
    }

    @Override
    public int deleteByID(int userID) {

        Optional<User> searchedUser = userRepository.findById(userID);

        User userToDelete = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User for deletion not Found:" + userID, "User"));

        userRepository.delete(userToDelete);

        return 1;
    }

    @Override
    public int deleteByUsername(String username) {

        Optional<User> searchedUser = userRepository.findUserByUserName(username);

        User userToDelete = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User for deletion not Found:" + username, "User"));

        userRepository.delete(userToDelete);

        return userToDelete.getUserID();
    }

    private List<UserDto> mapListToUserDto(List<User> listOfUsers) {

        return listOfUsers.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    private void isUserFirstOrLastNameValid(String userFirstOrLastName) {

        if (!userValidation.isUserFirstOrLastNameValid(userFirstOrLastName)) {

            throw new InvalidInputException("Invalid Name");
        }
    }

    private void isUserNameValid(String userName) {

        if (!userValidation.isUserNameValid(userName)) {

            throw new InvalidInputException("Invalid Username. It can only contain chars and numbers!");
        }
    }

    private void isUserListEmpty(List<User> userList) {

        if (userValidation.isListEmpty(userList)) {

            throw new ResourceNotFoundException("Users not Found", "User");
        }
    }

    private User getLoginUser(String userName, String password) {

        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(user -> user.getUserName().equals(userName) &&
                        passwordEncoderConfiguration.passwordEncoder().matches(password, user.getUserPassword()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Incorrect email or password!", "User"));
    }

    User findUserByID(int userID) {

        Optional<User> searchedUserOptional = userRepository.findById(userID);

        return searchedUserOptional.orElseThrow(() ->
                new ResourceNotFoundException("User ID not Found:" + userID, "User"));
    }
}
