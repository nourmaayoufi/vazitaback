package vazita.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "UTILISATEURS")
public class User {
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
    private String noma;

    @Column(name = "PRENOMA")
    private String prenoma;

    @Column(name = "COD_GRP")
    private Integer codGrp;

    @Column(name = "ID_CENTRE")
    private String idCentre;

    @Column(name = "DATE_DEB")
    private LocalDate dateDeb;

    @Column(name = "DATE_FIN")
    private LocalDate dateFin;

    @Column(name = "ETAT")
    private String etat;
}
