package dev.mikita.rolt.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * The type Request update review dto.
 */
public record ReviewUpdateRequestDTO(
    @Size(min = 10, max = 1000, message = "Review must be between 10 and 1000 characters")
    String description,
    @Min(value = 1, message = "The rating cannot be lower than one.")
    @Max(value = 5, message = "The rating cannot be higher than five.")
    Integer rating
) {}
