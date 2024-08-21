package dev.mikita.rolt.dto.response;

import java.time.LocalDateTime;

public record UserResponseDTO(
    Long id,
    String email,
    LocalDateTime createdOn,
    LocalDateTime lastLogin
) {}
