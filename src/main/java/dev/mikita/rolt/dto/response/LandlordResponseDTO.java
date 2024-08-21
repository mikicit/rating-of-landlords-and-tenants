package dev.mikita.rolt.dto.response;

import dev.mikita.rolt.model.ConsumerGender;
import java.time.LocalDateTime;

/**
 * The type Response public landlord dto.
 */
public record LandlordResponseDTO(
    Long id,
    LocalDateTime createdOn,
    String firstName,
    String lastName,
    ConsumerGender gender
) {}
