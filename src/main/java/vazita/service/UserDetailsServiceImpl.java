package vazita.service;


import vazita.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;  // Dependency for repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for: {}", username);
        
        // Fetch the user from the repository
        return utilisateurRepository.findByIdUser(username)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", username);  // Log warning if user not found
                    return new UsernameNotFoundException("User not found: " + username);  // Throw exception if not found
                });
    }
}
