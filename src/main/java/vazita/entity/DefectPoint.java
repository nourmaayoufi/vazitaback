package vazita.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;





@Entity
@Table(name = "POINTS_DEFAUTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectPoint {

    @Id
    @Column(name = "CODE_POINT")
    private Integer pointCode;
    
    @Column(name = "LIBELLE_POINT")
    private String pointLabel;
    
    @Column(name = "CODE_CHAPITRE")
    private Integer chapterCode;
}