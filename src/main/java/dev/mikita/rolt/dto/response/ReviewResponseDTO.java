package dev.mikita.rolt.dto.response;

import java.time.LocalDateTime;

/**
 * The type Response public review dto.
 */
public record ReviewResponseDTO(
    Long id,
    Long authorId,
    LocalDateTime createdOn,
    LocalDateTime updatedOn,
    Long contractId,
    String description,
    Integer rating
) {}
