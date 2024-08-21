package dev.mikita.rolt.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * The type Request create contract dto.
 */
public record ContractCreateRequestDTO(
    @Future(message = "The date must be in the future.")
    LocalDate startDate,
    @Future(message = "The date must be in the future.")
    LocalDate endDate,
    @NotNull(message = "Specify the property id.")
    Long propertyId
) {}
