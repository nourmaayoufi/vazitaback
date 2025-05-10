package vazita.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DOSSIER_DEFAUTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierDefect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DEFECT", insertable = false)
    private Long id;
    
    @Column(name = "DAT_CTRL")
    private Date controlDate;
    
    @Column(name = "NUM_CENTRE")
    private Integer centerNumber;
    
    @Column(name = "N_DOSSIER")
    private String dossierNumber;
    
    @Column(name = "DATE_HEURE_ENREGISTREMENT")
    private Date registrationTime;
    
    @Column(name = "NUM_CHASSIS")
    private String chassisNumber;
    
    @Column(name = "CODE_DEFAUT")
    private Integer defectCode;
    
    @Column(name = "MAT_AGENT")
    private String agentId;
}
