package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.request.PropertyCreateRequestDTO;
import dev.mikita.rolt.dto.request.PropertyUpdateRequestDTO;
import dev.mikita.rolt.dto.response.PropertyResponseDTO;
import dev.mikita.rolt.model.Property;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    PropertyResponseDTO toPropertyResponseDTO(Property property);
    Property toProperty(PropertyCreateRequestDTO propertyResponseDTO);
    void updatePropertyFromDto(PropertyUpdateRequestDTO propertyCreateRequestDTO, @MappingTarget Property property);
}
