package vazita.security;

import vazita.entity.User;
import vazita.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByIdUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Check if user is active (status == "A")
        if (!user.isActive()) {
            throw new UsernameNotFoundException("User is inactive: " + username);
        }

        // Map roles dynamically based on the user's role code
        List<GrantedAuthority> authorities = new ArrayList<>();
        String roleName = user.getRoleName();
        authorities.add(new SimpleGrantedAuthority(roleName));

        return new UserDetailsImpl(
                user.getIdUser(),
                user.getPassword(),
                user.getCenterId(),
                authorities
        );
    }
}
