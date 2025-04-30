package vazita.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity representing an inspection center, mapped to CENTRE_CVT table
 */
@Entity
@Table(name = "CENTRE_CVT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Center {

    @Id
    @Column(name = "ID_CENTRE")
    private String idCentre;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "MACHINE")
    private String machine;

    @Column(name = "SID")
    private String sid;
}