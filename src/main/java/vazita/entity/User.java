package vazita.entity;

import lombok.Data;
import jakarta.persistence.*;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.util.Date;

@Entity
@Table(name = "UTILISATEURS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "ID_USER")
    private String idUser;
    
    @Column(name = "PASSE")
    private String password;
    
    @Column(name = "NOM")
    private String lastName;
    
    @Column(name = "PRENOM")
    private String firstName;
    
    @Column(name = "NOMA")
    private String lastNameArabic;
    
    @Column(name = "PRENOMA")
    private String firstNameArabic;
    
    @Column(name = "COD_GRP")
    private Integer roleCode;
    
    @Column(name = "ID_CENTRE")
    private Integer centerId;
    
    @Column(name = "DATE_DEB")
    private Date startDate;
    
    @Column(name = "DATE_FIN")
    private Date endDate;
    
    @Column(name = "ETAT")
    private String status;
    public boolean isActive() {
        return "A".equals(this.status);
    }
    // Method to convert roleCode to Spring Security role name
    public String getRoleName() {
        switch (roleCode) {
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