package vazita.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import vazita.security.UserDetailsServiceImpl;
import java.io.Serializable;

@Component
public class CenterPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }

        UserDetailsServiceImpl userDetails = (UserDetailsServiceImpl) auth.getPrincipal();
        String userCenterId = userDetails.getCenterId(); // Changed to String based on the User model

        // For center-specific objects
        if (targetDomainObject instanceof String) {
            String targetCenterId = (String) targetDomainObject;

            // Check if user belongs to the target center
            return userCenterId.equals(targetCenterId);
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if (auth == null || targetType == null || !(permission instanceof String)) {
            return false;
        }

        if (targetId instanceof String && "CENTER".equals(targetType)) {
            return hasPermission(auth, targetId, permission);
        }

        return false;
    }
}
