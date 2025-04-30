package vazita.service;


import vazita.exception.ResourceNotFoundException;
import vazita.dto.UserDto;
import vazita.dto.UserRequest;
import vazita.entity.User;
import vazita.repository.UserRepository;
import vazita.security.UserDetailsImpl;
import vazita.util.CenterContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    private static final long TOKEN_EXPIRATION = 24 * 60 * 60; // 24 hours in seconds

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsersForCurrentCenter(Pageable pageable) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        Page<User> users = userRepository.findAllByCenterId(userDetails.getCenterId(), pageable);
        return users.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        User user = userRepository.findByIdAndCenterId(id, userDetails.getCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        return convertToDto(user);
    }

    @Transactional
    public UserDto createUser(UserRequest userRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword()); // Using plaintext as specified in requirements
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setFirstNameArabic(userRequest.getFirstNameArabic());
        user.setLastNameArabic(userRequest.getLastNameArabic());
        user.setGroupCode(userRequest.getGroupCode());
        user.setCenterId(userDetails.getCenterId());
        user.setStartDate(userRequest.getStartDate());
        user.setEndDate(userRequest.getEndDate());
        user.setStatus(userRequest.getStatus());
        
        User savedUser = userRepository.save(user);
        log.info("User created: {}", savedUser.getUsername());
        
        return convertToDto(savedUser);
    }

    @Transactional
    public UserDto updateUser(Long id, UserRequest userRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        User user = userRepository.findByIdAndCenterId(id, userDetails.getCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setFirstNameArabic(userRequest.getFirstNameArabic());
        user.setLastNameArabic(userRequest.getLastNameArabic());
        user.setGroupCode(userRequest.getGroupCode());
        user.setStartDate(userRequest.getStartDate());
        user.setEndDate(userRequest.getEndDate());
        user.setStatus(userRequest.getStatus());
        
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(userRequest.getPassword());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated: {}", updatedUser.getUsername());
        
        return convertToDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        User user = userRepository.findByIdAndCenterId(id, userDetails.getCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        userRepository.delete(user);
        log.info("User deleted: {}", user.getUsername());
    }

    public void invalidateToken(String token) {
        redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "blacklisted", TOKEN_EXPIRATION, TimeUnit.SECONDS);
        log.info("Token blacklisted");
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token));
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFirstNameArabic(user.getFirstNameArabic());
        dto.setLastNameArabic(user.getLastNameArabic());
        dto.setGroupCode(user.getGroupCode());
        dto.setCenterId(user.getCenterId());
        dto.setStartDate(user.getStartDate());
        dto.setEndDate(user.getEndDate());
        dto.setStatus(user.getStatus());
        return dto;
    }
}