package erkamber.services.interfaces;

import erkamber.dtos.RoleDto;
import erkamber.entities.Role;

import java.util.List;

public interface RoleService {

    RoleDto addRole(RoleDto newRole);

    RoleDto getByID(int roleID);

    RoleDto getByName(String roleName);

    RoleDto deleteByID(int roleID);

    RoleDto deleteByName(String roleName);

    RoleDto updateByID(int roleID, String newRoleName);

    List<RoleDto> getAll();

   // Role getRoleByUserID(int userID);
}
