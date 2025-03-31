package com.cd.userservice.dto;

import com.cd.userservice.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not well formatted")
    private String email;

    @NotNull(message = "Role can be STUDENT or INSTRUCTOR and is required")
    private Role role;

}
