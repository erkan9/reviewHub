package erkamber.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, insertable = false, nullable = false)
    private int roleID;

    @Column(name = "roles", length = 30, unique = true, updatable = true, insertable = true, nullable = false)
    private String role;

    @OneToMany
    List<User> users;
}
