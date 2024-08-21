package dev.mikita.rolt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ModeratorUpdateRequestDTO(
    @Email(message = "Email should be valid.")
    @NotEmpty(message = "Email cannot be empty.")
    String email,
    @Size(min = 2, max = 16, message = "The password must be between 2 and 16 characters.")
    String password
) {}