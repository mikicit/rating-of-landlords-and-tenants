package dev.mikita.rolt.dto.response;

import dev.mikita.rolt.model.ConsumerGender;
import java.time.LocalDateTime;

/**
 * The type Response public tenant dto.
 */
public record TenantResponseDTO(
    Long id,
    LocalDateTime createdOn,
    String firstName,
    String lastName,
    ConsumerGender gender,
    Boolean inSearch
) {}
