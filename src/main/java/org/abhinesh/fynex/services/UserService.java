package org.abhinesh.fynex.services;

import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.UpdateUserRequest;
import org.abhinesh.fynex.dto.UserResponse;
import org.abhinesh.fynex.entity.User;
import org.abhinesh.fynex.exception.ResourceNotFoundException;
import org.abhinesh.fynex.repository.UserRepository;
import org.abhinesh.fynex.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Get all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get user by id
    public UserResponse getUserById( Long id ){
        User user = findUser(id);
        return mapToResponse(user);
    }

    // Get current logged in User
    public UserResponse getCurrentUser(){
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToResponse(user);
    }

    // Update user role and status
    public UserResponse updateUser(Long id, UpdateUserRequest request){
        User user = findUser(id);

        // prevents admin from deactivating themselves
        String currentEmail = SecurityUtils.getCurrentUserEmail();
        if (user.getEmail().equals(currentEmail) && !request.getActive()){
            throw new RuntimeException("You cannot deactivate your own account");
        }
        user.setRole(request.getRole());
        user.setActive(request.getActive());

        return mapToResponse(userRepository.save(user));
    }

    // Activate user
    public UserResponse activateUser(Long id){
        User user = findUser(id);
        user.setActive(true);
        return mapToResponse(userRepository.save(user));
    }

    // Deactivate user
    public UserResponse deActivateUser (Long id){
        String currentEmail = SecurityUtils.getCurrentUserEmail();
        User user = findUser(id);

        // Prevent self deactivation
        if (user.getEmail().equals(currentEmail)){
            throw new RuntimeException("You cannot deactivate your own account");
        }

        user.setActive(false);
        return mapToResponse(userRepository.save(user));
    }

    // Delete user
    public void deleteUser(Long id){

        // Prevent self deletion
        String currentEmail = SecurityUtils.getCurrentUserEmail();
        User user = findUser(id);

        if (user.getEmail().equals(currentEmail)) {
            throw new RuntimeException("You cannot delete your own account");
        }
        user.setActive(false); //  soft delete
        userRepository.save(user);
    }

    // helper classes

    private User findUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id : " + id
                ));
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
