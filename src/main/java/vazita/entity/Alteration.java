package vazita.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
/**
 * Entity representing a specific alteration in the defect hierarchy, mapped to ALTERATIONS
 */
@Entity
@Table(name = "ALTERATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alteration {

    @Id
    @Column(name = "CODE_ALTERATION")
    private String codeAlteration;

    @Column(name = "LIBELLE_ALTERATION")
    private String libelleAlteration;

    @Column(name = "CODE_CHAPITRE")
    private String codeChapitre;

    @Column(name = "CODE_POINT")
    private String codePoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "CODE_CHAPITRE", referencedColumnName = "CODE_CHAPITRE", insertable = false, updatable = false),
        @JoinColumn(name = "CODE_POINT", referencedColumnName = "CODE_POINT", insertable = false, updatable = false)
    })
    private DefectPoint defectPoint;
}