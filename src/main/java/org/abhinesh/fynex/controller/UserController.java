package org.abhinesh.fynex.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.ApiResponse;
import org.abhinesh.fynex.dto.UpdateUserRequest;
import org.abhinesh.fynex.dto.UserResponse;
import org.abhinesh.fynex.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Any logged-in user - get their own profiles
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(){
        return ResponseEntity.ok(ApiResponse.success(
                "Profile fetched successfully",
                userService.getCurrentUser()
        ));
    }

    // Admin only - get all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        return ResponseEntity.ok(ApiResponse.success(
                "Users fetched successfully",
                userService.getAllUsers()
        ));
    }

    // Admin only - get user by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(
                "User fetched successfully",
                userService.getUserById(id)
        ));
    }

    // Admin - update user role and status
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
            ){
        return ResponseEntity.ok(ApiResponse.success(
                "User updated successfully",
                userService.updateUser(id,request)
        ));
    }

    // ADMIN only — activate user
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "User activated successfully",
                userService.activateUser(id)));
    }

    // ADMIN only — deactivate user
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "User deactivated successfully",
                userService.deActivateUser(id)));
    }

    // ADMIN only — delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

}
