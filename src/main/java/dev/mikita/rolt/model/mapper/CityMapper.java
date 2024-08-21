package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.request.CityUpdateRequestDTO;
import dev.mikita.rolt.dto.response.CityResponseDTO;
import dev.mikita.rolt.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityResponseDTO toCityResponseDTO(City city);
    City toCity(CityUpdateRequestDTO cityUpdateRequestDTO);
    void updateCityFromDTO(CityUpdateRequestDTO cityUpdateRequestDTO, @MappingTarget City city);
}