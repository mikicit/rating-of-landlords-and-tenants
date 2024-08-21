package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.response.ConsumerResponseDTO;
import dev.mikita.rolt.model.Consumer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumerMapper {
    ConsumerResponseDTO toConsumerResponseDTO(Consumer consumer);
}
