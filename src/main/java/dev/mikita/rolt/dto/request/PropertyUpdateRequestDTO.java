package dev.mikita.rolt.dto.request;

import dev.mikita.rolt.model.PropertyType;
import jakarta.validation.constraints.*;

/**
 * The type Request update property dto.
 */
public record PropertyUpdateRequestDTO(
    @NotNull(message = "Specify the property id.")
    Long id,
    @NotNull(message = "Specify the property type.")
    PropertyType propertyType,
    Boolean isAvailable,
    @Positive
    @Max(value = 10000, message = "Square should not be greater than 10000")
    @NotNull(message = "Specify the square.")
    Double square,
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    String description,
    @Size(min = 2, max = 64, message = "The street must be between 2 and 64 characters.")
    @NotBlank(message = "The street cannot be empty.")
    String street,
    @Size(min = 2, max = 24, message = "The postalCode must be between 2 and 24 characters.")
    @NotBlank(message = "The street cannot be empty.")
    String postalCode,
    @NotNull(message = "Specify the city id.")
    Long cityId
) {}