package org.abhinesh.fynex.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.abhinesh.fynex.enums.Role;

@Data
public class UpdateUserRequest {

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
