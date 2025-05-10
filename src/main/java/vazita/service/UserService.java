package vazita.service;

import vazita.dto.user.UserCreateRequest;
import vazita.dto.user.UserDto;
import vazita.dto.user.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    // Get all users for a specific center
    Page<UserDto> getAllUsersByCenter(Integer centerId, Pageable pageable);  // centerId should be Integer

    // Get a user by their ID
    UserDto getUserById(String userId);

    // Create a new user using UserCreateRequest
    UserDto createUser(UserCreateRequest createRequest);

    // Update existing user by ID using UserUpdateRequest
    UserDto updateUser(String userId, UserUpdateRequest updateRequest);

    // Delete user by ID
    void deleteUser(String userId);

    // Search users by center and a search term (name, etc.)
    Page<UserDto> searchUsersByCenter(Integer centerId, String searchTerm, Pageable pageable);  // centerId should be Integer

    // Get all users for the center of the currently logged-in user
    Page<UserDto> getAllUsersForCurrentCenter(Pageable pageable);

    // Get the currently logged-in user
    UserDto getCurrentUser();
}
