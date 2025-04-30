package vazita.security;


import vazita.entity.User;
import vazita.repository.UserRepository;
import vazita.util.CenterContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom implementation of Spring Security's UserDetailsService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Temporarily clear center context to ensure we're using the central database
        String previousCenterId = CenterContextHolder.getCenterId();
        CenterContextHolder.setCenterId("CENTRALE");
        
        try {
            User user = userRepository.findByIdUser(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            
            return UserDetailsImpl.build(user);
        } finally {
            // Restore previous center context
            CenterContextHolder.setCenterId(previousCenterId);
        }
    }
}