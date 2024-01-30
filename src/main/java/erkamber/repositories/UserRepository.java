package erkamber.repositories;


import erkamber.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUserNameAndUserPassword(String userName, String password);

    Optional<User> findUserByUserName(String username);

    Optional<User> findByUserEmail(String email);

    List<User> findUserByUserFirstName(String firstName);

    List<User> findUserByUserLastName(String username);

    @Query("SELECT u FROM User u JOIN u.role r WHERE r.roleID = :roleID")
    List<User> findUsersByRoleID(int roleID);
}
