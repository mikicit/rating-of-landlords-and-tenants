package dev.mikita.rolt.dto.contract;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResponsePublicContractDto {
    private Integer id;
    private LocalDateTime createdOn;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer propertyId;
    private Integer tenantId;
}
