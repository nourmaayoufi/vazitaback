package vazita.model;

import jakarta.persistence.*;
import lombok.*;

// Marks this class as a database entity (a table representation)
@Entity
// Maps this entity to the table named "CENTRE_CVT"
@Table(name = "CENTRE_CVT")
// Lombok annotations to generate getters, setters, constructors, and builder automatically
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentreCVT {

    // Defines the primary key (ID_CENTRE column)
    @Id
    @Column(name = "ID_CENTRE")
    private Integer idCentre;

    // Maps the USERNAME column (used for database connection username)
    @Column(name = "USERNAME")
    private String username;

    // Maps the PASSWORD column (used for database connection password)
    @Column(name = "PASSWORD")
    private String password;

    // Maps the MACHINE column (likely the database server hostname or IP)
    @Column(name = "MACHINE")
    private String machine;

    // Maps the SID column (Oracle database System ID)
    @Column(name = "SID")
    private String sid;
}
