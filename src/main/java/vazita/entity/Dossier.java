package vazita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Entity representing a car in the inspection queue, mapped to MES_DOSSIERS
 */
@Entity
@Table(name = "MES_DOSSIERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dossier {

    @Id
    @Column(name = "N_DOSSIER")
    private String nDossier;

    @Column(name = "NUM_CHASSIS")
    private String numChassis;

    @Column(name = "IMMATRICULATION")
    private String immatriculation;

    @Column(name = "C_PISTE")
    private String cPiste;

    @Column(name = "DATE_HEURE_ENREGISTREMENT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHeureEnregistrement;
}