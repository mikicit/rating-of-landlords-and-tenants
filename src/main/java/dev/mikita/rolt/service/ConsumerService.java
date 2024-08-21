package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.response.ConsumerResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.model.Consumer;
import dev.mikita.rolt.model.ConsumerStatus;
import dev.mikita.rolt.model.User;
import dev.mikita.rolt.model.mapper.ConsumerMapper;
import dev.mikita.rolt.repository.ConsumerRepository;
import dev.mikita.rolt.repository.ReviewRepository;
import dev.mikita.rolt.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.List;

/**
 * The type Consumer service.
 */
@Service
@Transactional
public class ConsumerService {
    private final ConsumerRepository consumerRepository;
    private final ReviewRepository reviewRepository;
    private final ConsumerMapper consumerMapper;

    @Autowired
    public ConsumerService(ConsumerRepository consumerRepository,
                           ConsumerMapper consumerMapper,
                           ReviewRepository reviewRepository) {
        this.consumerRepository = consumerRepository;
        this.reviewRepository = reviewRepository;
        this.consumerMapper = consumerMapper;
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<ConsumerResponseDTO> getAll(Pageable pageable) {
        Page<Consumer> result = consumerRepository.findAll(pageable);
        List<ConsumerResponseDTO> consumers = result.getContent().stream()
                .map(consumerMapper::toConsumerResponseDTO)
                .toList();

        return new PagedResponseDTO<>(
                consumers,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public ConsumerResponseDTO get(Long id) {
        return consumerMapper.toConsumerResponseDTO(consumerRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Consumer.class.getSimpleName(), id)));
    }

    @Transactional(readOnly = true)
    public Double getRating(Long id) {
        Consumer user = consumerRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Consumer.class.getSimpleName(), id));

        return reviewRepository.findAverageRatingByConsumer(user);
    }

    public void updateStatus(Long id, ConsumerStatus status) {
        Consumer user = consumerRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Consumer.class.getSimpleName(), id));

        user.setStatus(status);
    }

    public void removeByConsumer(Long id, Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Consumer user = (Consumer) userDetails.getUser();

        if (!user.getId().equals(id)) {
            throw new ValidationException("You can't delete another user");
        }

        user.setStatus(ConsumerStatus.DELETED);
    }
}
