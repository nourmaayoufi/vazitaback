package vazita.model.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "UTILISATEURS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    
    @Id
    @Column(name = "ID_USER")
    private String idUser;
    
    @Column(name = "PASSE")
    private String passe;
    
    @Column(name = "NOM")
    private String nom;
    
    @Column(name = "PRENOM")
    private String prenom;
    
    @Column(name = "NOMA")
    private String nomA;
    
    @Column(name = "PRENOMA")
    private String prenomA;
    
    @Column(name = "COD_GRP")
    private String codGrp;
    
    @Column(name = "ID_CENTRE")
    private String idCentre;
    
    @Column(name = "DATE_DEB")
    @Temporal(TemporalType.DATE)
    private Date dateDeb;
    
    @Column(name = "DATE_FIN")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    
    @Column(name = "ETAT")
    private String etat;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_GRP", referencedColumnName = "COD_GRP", insertable = false, updatable = false)
    private Groupe groupe;
}