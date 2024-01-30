package erkamber.repositories;

import erkamber.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findRoleByRole(String role);

    @Query("SELECT u.role FROM User u WHERE u.userID = :userID")
    Optional<Role> findRoleByUserID(@Param("userID") int userID);
}
