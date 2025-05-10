package vazita.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "MES_DOSSIERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDossier {

    @Id
    @Column(name = "N_DOSSIER")
    private String dossierNumber;
    
    @Column(name = "NUM_CHASSIS")
    private String chassisNumber;
    
    @Column(name = "IMMATRICULATION")
    private String registrationNumber;
    
    @Column(name = "C_PISTE")
    private String trackCode;
    
    @Column(name = "DATE_HEURE_ENREGISTREMENT")
    private Date registrationTime;
}
