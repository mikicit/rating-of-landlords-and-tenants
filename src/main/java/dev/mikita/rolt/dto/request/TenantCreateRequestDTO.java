package dev.mikita.rolt.dto.request;

import dev.mikita.rolt.model.ConsumerGender;
import jakarta.validation.constraints.*;

/**
 * The type Request create tenant dto.
 */
public record TenantCreateRequestDTO(
    @Email(message = "Email should be valid.")
    @NotEmpty(message = "Email cannot be empty.")
    String email,
    @Size(min = 2, max = 16, message = "The firstname must be between 2 and 16 characters.")
    String password,
    @Size(min = 2, max = 16, message = "The firstname must be between 2 and 16 characters.")
    String firstName,
    @Size(min = 2, max = 16, message = "The lastname must be between 2 and 16 characters.")
    String lastName,
    @NotBlank(message = "The phone cannot be empty.")
    String phone,
    @NotNull(message = "Set the gender.")
    ConsumerGender gender,
    Boolean inSearch
) {}
