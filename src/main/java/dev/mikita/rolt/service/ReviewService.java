package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.request.ReviewCreateRequestDTO;
import dev.mikita.rolt.dto.request.ReviewUpdateRequestDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.dto.response.ReviewResponseDTO;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.model.PublicationStatus;
import dev.mikita.rolt.model.Review;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.model.mapper.ReviewMapper;
import dev.mikita.rolt.repository.ConsumerRepository;
import dev.mikita.rolt.repository.ContractRepository;
import dev.mikita.rolt.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * The type Review service.
 */
@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ContractRepository contractRepository;
    private final ConsumerRepository consumerRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         ContractRepository contractRepository,
                         ConsumerRepository consumerRepository,
                         ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.contractRepository = contractRepository;
        this.consumerRepository = consumerRepository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @param filters  the filters
     * @return the page
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<ReviewResponseDTO> getAll(Pageable pageable, Map<String, Object> filters) {
        Specification<Review> spec = Specification.where(null);

        if (filters.containsKey("status")) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), filters.get("status")));
        }

        if (filters.containsKey("authorId")) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("author"), consumerRepository.getReferenceById((Long) filters.get("authorId"))));
        }

        if (filters.containsKey("contractId")) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("contract"), contractRepository.getReferenceById((Long) filters.get("contractId"))));
        }

        Page<Review> page = reviewRepository.findAll(spec, pageable);
        List<ReviewResponseDTO> content = page.map(reviewMapper::toReviewResponseDTO).getContent();

        return new PagedResponseDTO<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );


    }

    /**
     * Find review.
     *
     * @param id the id
     * @return the review
     */
    @Transactional(readOnly = true)
    public ReviewResponseDTO get(Long id) {
        return reviewMapper.toReviewResponseDTO(reviewRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Review.class.getSimpleName(), id)));
    }

    public void add(ReviewCreateRequestDTO dto) {
        List<Review> reviews = reviewRepository
                .findByContractAndAuthor(
                        contractRepository.getReferenceById(dto.contractId()),
                        consumerRepository.getReferenceById(dto.authorId()));

        if (!reviews.isEmpty()) {
            throw new ValidationException("You have already left feedback for this contract.");
        }

        Review review = reviewMapper.toReview(dto);
        reviewRepository.save(review);
    }

    public void update(Long id, ReviewUpdateRequestDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Review.class.getSimpleName(), id));

        reviewMapper.updateReviewFromDto(dto, review);
    }

    public void remove(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Review.class.getSimpleName(), id));

        review.setStatus(PublicationStatus.DELETED);
    }

    public void publish(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Review.class.getSimpleName(), id));

        review.setStatus(PublicationStatus.PUBLISHED);
    }

    public void moderate(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Review.class.getSimpleName(), id));

        review.setStatus(PublicationStatus.MODERATION);
    }
}
