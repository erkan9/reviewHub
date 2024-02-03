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
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, insertable = false, nullable = false)
    private int categoryID;

    @Column(name = "category", length = 15, unique = true, updatable = true, insertable = true, nullable = false)
    private String category;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Venue> venue;
}
