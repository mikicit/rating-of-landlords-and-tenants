package dev.mikita.rolt.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The type Response public contract dto.
 */
public record ContractResponseDTO(
    Long id,
    LocalDateTime createdOn,
    LocalDate startDate,
    LocalDate endDate,
    Long propertyId,
    Long tenantId
) {}