package vazita.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;




@Entity
@Table(name = "ALTERATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alteration {

    @Id
    @Column(name = "CODE_ALTERATION")
    private Integer alterationCode;
    
    @Column(name = "LIBELLE_ALTERATION")
    private String alterationLabel;
    
    @Column(name = "CODE_CHAPITRE")
    private Integer chapterCode;
    
    @Column(name = "CODE_POINT")
    private Integer pointCode;
}