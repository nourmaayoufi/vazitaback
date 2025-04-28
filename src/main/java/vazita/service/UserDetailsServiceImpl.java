package vazita.service;



import vazita.model.entity.Utilisateur;
import vazita.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByIdUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        // Check if user is active
        if (!"A".equals(utilisateur.getEtat())) {
            throw new UsernameNotFoundException("User is not active: " + username);
        }
        
        return new User(
                utilisateur.getIdUser(),
                utilisateur.getPasse(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utilisateur.getCodGrp()))
        );
    }
}