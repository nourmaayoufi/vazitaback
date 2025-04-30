package vazita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import jakarta.persistence.*;
/**
 * Entity representing a submitted defect report, mapped to DOSSIER_DEFAUTS
 */
@Entity
@Table(name = "DOSSIER_DEFAUTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierDefaut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DAT_CTRL")
    @Temporal(TemporalType.DATE)
    private Date datCtrl;

    @Column(name = "NUM_CENTRE")
    private String numCentre;

    @Column(name = "N_DOSSIER")
    private String nDossier;

    @Column(name = "DATE_HEURE_ENREGISTREMENT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHeureEnregistrement;

    @Column(name = "NUM_CHASSIS")
    private String numChassis;

    @Column(name = "CODE_DEFAUT")
    private String codeDefaut;

    @Column(name = "MAT_AGENT")
    private String matAgent;
}
