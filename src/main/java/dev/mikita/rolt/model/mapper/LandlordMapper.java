package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.request.LandlordCreateRequestDTO;
import dev.mikita.rolt.dto.request.LandlordUpdateRequestDTO;
import dev.mikita.rolt.dto.response.LandlordResponseDTO;
import dev.mikita.rolt.model.Landlord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LandlordMapper {
    LandlordResponseDTO toLandlordResponseDTO(Landlord landlord);
    Landlord toLandlord(LandlordCreateRequestDTO landlordResponseDTO);
    void updateLandlordFromDTO(LandlordUpdateRequestDTO landlordUpdateRequestDTO, @MappingTarget Landlord landlord);
}
