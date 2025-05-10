package vazita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;




@Entity
@Table(name = "CHAPITRES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @Column(name = "CODE_CHAPITRE")
    private Integer chapterCode;
    
    @Column(name = "LIBELLE_CHAPITRE")
    private String chapterLabel;
}
