package vazita.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
// Marks this class as a database entity (a table representation)
@EnableJpaRepositories(basePackages = "vazita.repository") // Explicitly enabling repository scanning
@EntityScan(basePackages = "vazita.model") // Explicitly specifying entity package// Maps this entity to the table named "UTILISATEURS"
@Table(name = "UTILISATEURS")
// Lombok annotations to generate getters, setters, constructors, and builder automatically
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur implements UserDetails {

    // Defines the primary key (ID_USER column)
    @Id
    @Column(name = "ID_USER")
    private String idUser;

    // Maps the PASSE column (password of the user)
    @Column(name = "PASSE", nullable = false)
    private String passe;

    // Maps the NOM column (last name of the user)
    @Column(name = "NOM")
    private String nom;

    // Maps the PRENOM column (first name of the user)
    @Column(name = "PRENOM")
    private String prenom;

    // Maps the NOMA column (last name alias for the user)
    @Column(name = "NOMA")
    private String nomA;

    // Maps the PRENOMA column (first name alias for the user)
    @Column(name = "PRENOMA")
    private String prenomA;

    // Maps the COD_GRP column (group code of the user)
    @Column(name = "COD_GRP")
    private String codGrp;

    // Maps the ID_CENTRE column (center ID where the user belongs)
    @Column(name = "ID_CENTRE")
    private Integer idCentre;

    // Maps the DATE_DEB column (start date of the user's role or access)
    @Column(name = "DATE_DEB")
    private LocalDate dateDeb;

    // Maps the DATE_FIN column (end date of the user's role or access)
    @Column(name = "DATE_FIN")
    private LocalDate dateFin;

    // Maps the ETAT column (status of the user, e.g., active or inactive)
    @Column(name = "ETAT")
    private String etat;

    // Many-to-one relationship with the Groupe entity (group that the user belongs to)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_GRP", referencedColumnName = "COD_GRP", insertable = false, updatable = false)
    private Groupe groupe;

    // Spring Security method to get authorities (roles of the user)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.codGrp));
    }

    // Spring Security method to get the password of the user
    @Override
    public String getPassword() {
        return this.passe;
    }

    // Spring Security method to get the username (ID_USER of the user)
    @Override
    public String getUsername() {
        return this.idUser;
    }

    // Spring Security method to check if the account is expired (based on dates)
    @Override
    public boolean isAccountNonExpired() {
        LocalDate now = LocalDate.now();
        // Check if account's expiration date is in the future or not defined
        return (dateFin == null || dateFin.isAfter(now)) && 
               (dateDeb == null || !dateDeb.isAfter(now));
    }

    // Spring Security method to check if the account is locked (inactive)
    @Override
    public boolean isAccountNonLocked() {
        // Assuming "A" means active status
        return "A".equals(etat); 
    }

    // Spring Security method to check if the credentials (password) are expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming the credentials never expire
    }

    // Spring Security method to check if the account is enabled (not expired or locked)
    @Override
    public boolean isEnabled() {
        return isAccountNonExpired() && isAccountNonLocked();
    }
}
