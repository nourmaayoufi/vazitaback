package vazita.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

// Marks this class as a database entity (a table representation)
@Entity
// Maps this entity to the table named "GROUPE"
@Table(name = "GROUPE")
// Lombok annotations to generate getters, setters, constructors, and builder automatically
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Groupe {

    // Defines the primary key (COD_GRP column)
    @Id
    @Column(name = "COD_GRP")
    private String codGrp;

    // Maps the DESIGNATION column (likely the name or title of the group)
    @Column(name = "DESIGNATION")
    private String designation;

    // One-to-many relationship with the Utilisateur (User) entity
    // This represents the list of users that belong to this group
    // `mappedBy = "groupe"` indicates that the "groupe" field in the Utilisateur entity 
    // is the owning side of the relationship (which should be a reference to Groupe)
    @OneToMany(mappedBy = "groupe", fetch = FetchType.LAZY)
    private List<Utilisateur> utilisateurs;
}
