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
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, insertable = false, nullable = false)
    private int venueID;

    @Column(name = "name", length = 40, unique = true, updatable = true, insertable = true, nullable = false)
    private String venueName;

    @Column(name = "description", length = 300, unique = false, updatable = true, insertable = true, nullable = false)
    private String venueDescription;

    @Column(name = "city", length = 50, unique = false, updatable = true, insertable = true, nullable = false)
    private String venueCity;

    @Column(name = "address", length = 100, unique = true, updatable = true, insertable = true, nullable = false)
    private String venueAddress;

    @Column(name = "phone", length = 20, unique = true, updatable = true, insertable = true, nullable = false)
    private String phoneNumber;

    @Column(name = "email", length = 20, unique = true, updatable = true, insertable = true, nullable = false)
    private String email;

    @Column(name = "rating", unique = false, updatable = true, insertable = true, nullable = false)
    private double averageRating = 0;

    @Column(name = "review_count", unique = false, updatable = true, insertable = true, nullable = false)
    int reviewCount = 0;

    @ManyToOne
    private Category category;

    @OneToMany(fetch = FetchType.EAGER)
    List<Review> venueReviews;
}
