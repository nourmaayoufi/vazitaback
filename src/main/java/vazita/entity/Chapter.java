package vazita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
/**
 * Entity representing a chapter in the defect hierarchy, mapped to CHAPITRES
 */
@Entity
@Table(name = "CHAPITRES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @Column(name = "CODE_CHAPITRE")
    private String codeChapitre;

    @Column(name = "LIBELLE_CHAPITRE")
    private String libelleChapitre;
}
