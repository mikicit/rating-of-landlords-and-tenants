package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.request.ModeratorCreateRequestDTO;
import dev.mikita.rolt.dto.request.ModeratorUpdateRequestDTO;
import dev.mikita.rolt.dto.response.ModeratorResponseDTO;
import dev.mikita.rolt.model.Moderator;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ModeratorMapper {
    ModeratorResponseDTO toModeratorResponseDTO(Moderator moderator);
    Moderator toModerator(ModeratorCreateRequestDTO moderatorResponseDTO);
    void updateModeratorFromDTO(ModeratorUpdateRequestDTO moderatorCreateRequestDTO, @MappingTarget Moderator moderator);
}
