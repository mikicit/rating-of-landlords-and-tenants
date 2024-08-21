package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.response.UserResponseDTO;
import dev.mikita.rolt.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserResponseDTO(User user);
}
