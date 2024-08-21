package dev.mikita.rolt.dto.response;

import dev.mikita.rolt.model.PropertyType;
import java.time.LocalDateTime;

/**
 * The type Response public property dto.
 */
public record PropertyResponseDTO(
    Long id,
    Long ownerId,
    LocalDateTime createdOn,
    LocalDateTime updatedOn,
    PropertyType propertyType,
    Boolean isAvailable,
    Double square,
    String description,
    String street,
    String postalCode,
    Long cityId
) {}
