package erkamber.entities;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, insertable = false, nullable = false)
    private int userID;

    @Column(name = "username", length = 20, unique = true, updatable = true, insertable = true, nullable = false)
    private String userName;

    @Column(name = "user_email", length = 30, unique = true, updatable = true, insertable = true, nullable = false)
    private String userEmail;

    @Column(name = "first_name", length = 20, unique = false, updatable = true, insertable = true, nullable = false)
    private String userFirstName;

    @Column(name = "last_name", length = 20, unique = false, updatable = true, insertable = true, nullable = false)
    private String userLastName;

    @Column(name = "password", length = 500, unique = false, updatable = true, insertable = true, nullable = false)
    private String userPassword;

    @ManyToOne
    Role role;
}
