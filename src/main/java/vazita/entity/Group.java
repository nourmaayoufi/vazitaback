package vazita.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Entity representing a user group/role, mapped to GROUPE table
 */
@Entity
@Table(name = "GROUPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @Column(name = "COD_GRP")
    private Integer codGrp;

    @Column(name = "DESIGNATION")
    private String designation;
}
