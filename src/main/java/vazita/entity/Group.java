package vazita.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;






@Entity
@Table(name = "GROUPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @Column(name = "COD_GRP")
    private Integer codGrp;
    
    @Column(name = "DESIGNATION")
    private String designation;
    
    // Helper method to map code to role name
    public String getRoleName() {
        switch (codGrp) {
            case 1:
                return "ROLE_ADMIN";
            case 2:
                return "ROLE_INSPECTOR";
            case 3:
                return "ROLE_ADJOINT";
            default:
                return "ROLE_UNKNOWN";
        }
    }
}