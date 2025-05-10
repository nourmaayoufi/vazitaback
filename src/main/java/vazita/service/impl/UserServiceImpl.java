package vazita.service.impl;

import vazita.dto.user.UserCreateRequest;
import vazita.dto.user.UserDto;
import vazita.dto.user.UserUpdateRequest;
import vazita.entity.User;
import vazita.repository.UserRepository;
import vazita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<UserDto> getAllUsersByCenter(Integer centerId, Pageable pageable) {
        List<User> users = userRepository.findAllByIdCentre(centerId);  // Fetching users by centerId
        List<UserDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, users.size());
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findByIdUser(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest request) {
        User user = new User();
        user.setIdUser(request.getIdUser());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setLastNameArabic(request.getLastNameArabic());
        user.setFirstNameArabic(request.getFirstNameArabic());
        user.setRoleCode(request.getRoleCode());
        user.setCenterId(request.getCenterId());  // Center ID should be Integer
        user.setStartDate(request.getStartDate());
        user.setEndDate(request.getEndDate());
        user.setStatus(request.getStatus());
        user.setPassword(request.getPassword());  // Password stored as plain text

        userRepository.save(user);
        return convertToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findByIdUser(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setLastNameArabic(request.getLastNameArabic());
        user.setFirstNameArabic(request.getFirstNameArabic());
        user.setRoleCode(request.getRoleCode());
        user.setStartDate(request.getStartDate());
        user.setEndDate(request.getEndDate());
        user.setStatus(request.getStatus());

        userRepository.save(user);
        return convertToDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findByIdUser(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public Page<UserDto> searchUsersByCenter(Integer centerId, String searchTerm, Pageable pageable) {
        List<User> users = userRepository.searchUsersByCentre(searchTerm, centerId);  // Searching users by center and search term
        List<UserDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, users.size());
    }

    @Override
    public Page<UserDto> getAllUsersForCurrentCenter(Pageable pageable) {
        Integer centerId = getCurrentUserCenterId();
        List<User> users = userRepository.findAllByIdCentre(centerId);
        List<UserDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, users.size());
    }

    @Override
    public UserDto getCurrentUser() {
        String userId = getAuthenticatedUsername();
        User user = userRepository.findByIdUser(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return auth.getName();
    }

    private Integer getCurrentUserCenterId() {
        String userId = getAuthenticatedUsername();
        User user = userRepository.findByIdUser(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getCenterId();
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getIdUser(),
                user.getLastName(),
                user.getFirstName(),
                user.getLastNameArabic(),
                user.getFirstNameArabic(),
                user.getRoleCode(),
                getRoleName(user),  // Ensure proper role handling
                user.getCenterId(),
                user.getStartDate(),
                user.getEndDate(),
                user.getStatus(),
                isUserActive(user.getStartDate(), user.getEndDate())
        );
    }

    private String getRoleName(User user) {
        // Assuming role names can be derived from role code or another field
        return "ROLE_" + user.getRoleCode();  // Example role mapping, adjust as needed
    }

    private boolean isUserActive(Date start, Date end) {
        Date now = new Date();
        return (start == null || !now.before(start)) && (end == null || !now.after(end));
    }
}
