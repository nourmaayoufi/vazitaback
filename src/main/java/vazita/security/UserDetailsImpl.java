package vazita.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import vazita.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Spring Security's UserDetails for our application
 */
@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    
    @JsonIgnore
    private String password;
    
    private String nom;
    private String prenom;
    private String centerId;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Create UserDetailsImpl from User entity
     */
    public static UserDetailsImpl build(User user) {
        // Map group code to role name
        String roleName;
        switch (user.getCodGrp()) {
            case 1:
                roleName = "ROLE_ADMIN";
                break;
            case 2:
                roleName = "ROLE_INSPECTOR";
                break;
            case 3:
                roleName = "ROLE_ADJOINT";
                break;
            default:
                roleName = "ROLE_USER";
                break;
        }
        
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(roleName)
        );

        return new UserDetailsImpl(
                user.getIdUser(),
                user.getIdUser(), // Username is the same as ID in our case
                user.getPasse(),
                user.getNom(),
                user.getPrenom(),
                user.getIdCentre(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
