package vazita.model.entity;

import jakarta.persistence.*;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "GROUPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Groupe {
    
    @Id
    @Column(name = "COD_GRP")
    private String codGrp;
    
    @Column(name = "DESIGNATION")
    private String designation;
}