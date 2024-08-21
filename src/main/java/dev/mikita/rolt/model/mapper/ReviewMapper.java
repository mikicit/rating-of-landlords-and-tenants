package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.request.ReviewCreateRequestDTO;
import dev.mikita.rolt.dto.request.ReviewUpdateRequestDTO;
import dev.mikita.rolt.dto.response.ReviewResponseDTO;
import dev.mikita.rolt.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewResponseDTO toReviewResponseDTO(Review review);
    Review toReview(ReviewCreateRequestDTO reviewResponseDTO);
    void updateReviewFromDto(ReviewUpdateRequestDTO reviewUpdateRequestDTO, @MappingTarget Review review);
}
