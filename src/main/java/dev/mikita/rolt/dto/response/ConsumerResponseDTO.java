package dev.mikita.rolt.dto.response;

import dev.mikita.rolt.model.ConsumerGender;
import dev.mikita.rolt.model.ConsumerStatus;
import dev.mikita.rolt.model.Role;

import java.time.LocalDateTime;

public record ConsumerResponseDTO(
    Long id,
    String email,
    LocalDateTime createdOn,
    LocalDateTime lastLogin,
    Role role,
    String firstName,
    String lastName,
    String phone,
    ConsumerGender gender,
    ConsumerStatus status
) {}
