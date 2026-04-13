package org.abhinesh.fynex.dto;

import lombok.Builder;
import lombok.Data;
import org.abhinesh.fynex.enums.Role;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
}
