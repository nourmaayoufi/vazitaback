package vazita.security;

import com.fasterxml.jackson.databind.ObjectMapper;  // For serializing Java objects into JSON
import jakarta.servlet.http.HttpServletRequest;  // For accessing HTTP request data
import jakarta.servlet.http.HttpServletResponse; // For sending HTTP responses
import lombok.RequiredArgsConstructor;  // For generating constructor-based dependency injection
import org.springframework.http.MediaType;  // For setting content type to JSON
import org.springframework.security.core.AuthenticationException;  // To handle authentication-related exceptions
import org.springframework.security.web.AuthenticationEntryPoint;  // Interface for handling unauthenticated requests
import org.springframework.stereotype.Component;  // Marks this class as a Spring Bean

import java.io.IOException;  // For handling I/O operations
import java.util.HashMap;  // For storing response data in a map
import java.util.Map;  // For storing the response body

// Marks this class as a Spring Bean so it can be injected wherever needed
@Component
@RequiredArgsConstructor  // Lombok annotation to generate the constructor with required arguments
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;  // ObjectMapper for converting Java objects to JSON

    // This method is called when an unauthenticated user attempts to access a protected resource
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        // Set the response content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Set the HTTP response status to 401 (Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Prepare the response body with relevant details
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);  // Status code
        body.put("error", "Unauthorized");  // Error type
        body.put("message", authException.getMessage());  // Message from the authentication exception
        body.put("path", request.getServletPath());  // Path of the requested resource

        // Write the response body to the output stream as JSON
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
