package erkamber.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, insertable = false, nullable = false)
    private int reviewID;

    @Column(name = "description", length = 400, unique = false, updatable = true, insertable = true, nullable = false)
    private String description;

    @Column(name = "rating", unique = false, updatable = true, insertable = true, nullable = false)
    private int rating = 0;

    @OneToOne
    private User author;

    @ManyToOne
    private Venue venue;
}
