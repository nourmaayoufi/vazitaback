package vazita.service;

import vazita.dto.auth.JwtResponse;
import vazita.dto.auth.LoginRequest;
import vazita.dto.auth.TokenRefreshRequest;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse refreshToken(TokenRefreshRequest refreshRequest);
    void logout(String token);
}