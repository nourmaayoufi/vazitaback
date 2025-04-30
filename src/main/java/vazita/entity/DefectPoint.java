package vazita.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
/**
 * Entity representing a defect point in the hierarchy, mapped to POINTS_DEFAUTS
 */
@Entity
@Table(name = "POINTS_DEFAUTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectPoint {

    @Id
    @Column(name = "CODE_POINT")
    private String codePoint;

    @Column(name = "LIBELLE_POINT")
    private String libellePoint;

    @Column(name = "CODE_CHAPITRE")
    private String codeChapitre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODE_CHAPITRE", referencedColumnName = "CODE_CHAPITRE", insertable = false, updatable = false)
    private Chapter chapter;
}