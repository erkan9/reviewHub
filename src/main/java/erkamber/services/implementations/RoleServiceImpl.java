package erkamber.services.implementations;

import erkamber.dtos.RoleDto;
import erkamber.entities.Role;
import erkamber.exceptions.InvalidInputException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.repositories.RoleRepository;
import erkamber.services.interfaces.RoleService;
import erkamber.validations.RoleValidation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleValidation roleValidation;

    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleValidation roleValidation, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.roleValidation = roleValidation;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleDto addRole(RoleDto newRole) {

        isRoleNameValid(newRole.getRole());

        Role role = modelMapper.map(newRole, Role.class);

        roleRepository.save(role);

        return newRole;
    }

    @Override
    public RoleDto getByID(int roleID) {

        Optional<Role> searchedRoleOptional = roleRepository.findById(roleID);

        Role searchedRole = searchedRoleOptional.orElseThrow(() ->
                new ResourceNotFoundException("Role with ID not Found:" + roleID, "Role"));

        return modelMapper.map(searchedRole, RoleDto.class);
    }

    @Override
    public RoleDto getByName(String roleName) {

        Optional<Role> searchedRoleOptional = roleRepository.findRoleByRole(roleName);

        Role searchedRole = searchedRoleOptional.orElseThrow(() ->
                new ResourceNotFoundException("Role not Found:" + roleName, "Role"));

        return modelMapper.map(searchedRole, RoleDto.class);
    }

    @Override
    public RoleDto deleteByID(int roleID) {

        Optional<Role> searchedRoleOptional = roleRepository.findById(roleID);

        Role searchedRole = searchedRoleOptional.orElseThrow(() ->
                new ResourceNotFoundException("Role with ID not Found:" + roleID, "Role"));

        roleRepository.delete(searchedRole);

        return modelMapper.map(searchedRole, RoleDto.class);
    }

    @Override
    public RoleDto deleteByName(String roleName) {

        Optional<Role> searchedRoleOptional = roleRepository.findRoleByRole(roleName);

        Role searchedRole = searchedRoleOptional.orElseThrow(() ->
                new ResourceNotFoundException("Role not Found:" + roleName, "Role"));

        roleRepository.delete(searchedRole);

        return modelMapper.map(searchedRole, RoleDto.class);
    }

    @Override
    public RoleDto updateByID(int roleID, String newRoleName) {

        Optional<Role> searchedRoleOptional = roleRepository.findById(roleID);

        Role searchedRole = searchedRoleOptional.orElseThrow(() ->
                new ResourceNotFoundException("Role with ID not Found:" + roleID, "Role"));

        searchedRole.setRole(newRoleName);

        roleRepository.save(searchedRole);

        return modelMapper.map(searchedRole, RoleDto.class);
    }

    @Override
    public List<RoleDto> getAll() {

        List<Role> allRoles = roleRepository.findAll();

        return mapListToRoleDto(allRoles);
    }

   // @Override
    //public Role getRoleByUserID(int userID) {

//        Optional<Role> searchedRoleOptional = roleRepository.findRoleByUserID(userID);
//
//        return searchedRoleOptional.orElseThrow(() ->
//                new ResourceNotFoundException("Role not found for User ID:" + userID, "Role"));

       // return null;
   // }

    private List<RoleDto> mapListToRoleDto(List<Role> listOfRoles) {

        return listOfRoles.stream()
                .map(roles -> modelMapper.map(roles, RoleDto.class))
                .collect(Collectors.toList());
    }

    private void isRoleNameValid(String roleName) {

        if (!roleValidation.isRoleValid(roleName)) {

            throw new InvalidInputException("Role name must contain only characters");
        }
    }

    Role getRoleByID(int roleID) {

        Optional<Role> searchedRoleOptional = roleRepository.findById(roleID);

        Role searchedRole = searchedRoleOptional.orElseThrow(() ->
                new ResourceNotFoundException("Role with ID not Found:" + roleID, "Role"));

        return searchedRole;
    }
}
