package vazita.model.entity;

import jakarta.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "CENTRE_CVT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentreCVT {
    
    @Id
    @Column(name = "ID_CENTRE")
    private String idCentre;
    
    @Column(name = "USERNAME")
    private String username;
    
    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "MACHINE")
    private String machine;
    
    @Column(name = "SID")
    private String sid;
}