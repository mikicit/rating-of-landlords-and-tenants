package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.request.TenantCreateRequestDTO;
import dev.mikita.rolt.dto.request.TenantUpdateRequestDTO;
import dev.mikita.rolt.dto.response.TenantResponseDTO;
import dev.mikita.rolt.model.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TenantMapper {
    TenantResponseDTO toTenantResponseDTO(Tenant tenant);
    Tenant toTenant(TenantCreateRequestDTO tenantResponseDTO);
    void updateTenantFromDto(TenantUpdateRequestDTO tenantUpdateRequestDTO, @MappingTarget Tenant tenant);
}
