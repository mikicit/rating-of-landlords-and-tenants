package dev.mikita.rolt.rest;

import dev.mikita.rolt.dto.property.ResponsePublicPropertyDto;
import dev.mikita.rolt.dto.review.RequestCreateReviewDto;
import dev.mikita.rolt.dto.review.RequestUpdateReviewDto;
import dev.mikita.rolt.dto.review.ResponsePublicReviewDto;
import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.service.ConsumerService;
import dev.mikita.rolt.service.ContractService;
import dev.mikita.rolt.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/reviews")
public class ReviewController {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;
    private final ContractService contractService;
    private final ConsumerService consumerService;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ContractService contractService,
                            ConsumerService consumerService) {
        this.reviewService = reviewService;
        this.contractService = contractService;
        this.consumerService = consumerService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponsePublicReviewDto getReview(@PathVariable Integer id) {
        final Review review = reviewService.find(id);
        if (review == null)
            throw NotFoundException.create("Review", id);
        return new ModelMapper().map(review, ResponsePublicReviewDto.class);
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) PublicationStatus status,
            @RequestParam(required = false) Integer authorId,
            @RequestParam(required = false) Integer reviewedId,
            @RequestParam(required = false) Integer contractId) {

        ModelMapper modelMapper = new ModelMapper();

        // Filters
        Map<String, Object> filters = new HashMap<>();
        if (status != null) filters.put("status", status);
        if (authorId != null) filters.put("authorId", authorId);
        if (reviewedId != null) filters.put("reviewedId", reviewedId);
        if (contractId != null) filters.put("contractId", contractId);

        // Pagination and sorting
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> pageReviews = reviewService.findAll(pageable, filters);
        List<Review> reviews = pageReviews.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviews.stream()
                .map(review -> modelMapper.map(review, ResponsePublicReviewDto.class))
                .collect(Collectors.toList()));
        response.put("currentPage", pageReviews.getNumber());
        response.put("totalItems", pageReviews.getTotalElements());
        response.put("totalPages", pageReviews.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_TENANT')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addReview(@RequestBody @Valid RequestCreateReviewDto reviewDto) {
        final Contract contract = contractService.find(reviewDto.getContractId());
        if (contract == null)
            throw NotFoundException.create("Contract", reviewDto.getContractId());

        final Consumer consumer = consumerService.find(reviewDto.getAuthorId());
        if (consumer == null)
            throw NotFoundException.create("Consumer", reviewDto.getAuthorId());

        ModelMapper modelMapper = new ModelMapper();
        // Due to Consumer is abstract
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(new PropertyMap<RequestCreateReviewDto, Review>() {
            @Override
            protected void configure() {
                skip(destination.getAuthor());
            }
        });

        Review review = modelMapper.map(reviewDto, Review.class);
        review.setContract(contract);
        review.setAuthor(consumer);
        reviewService.persist(review);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", review.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateReview(@PathVariable Integer id, @RequestBody @Valid RequestUpdateReviewDto reviewDto) {
        final Review original = reviewService.find(id);
        if (original == null) {
            throw NotFoundException.create("Review", id);
        }

        if (!original.getId().equals(reviewDto.getId())) {
            throw new ValidationException("Review identifier in the data does not match the one in the request URL.");
        }

        final Contract contract = contractService.find(reviewDto.getContractId());
        if (contract == null)
            throw NotFoundException.create("Contract", reviewDto.getContractId());

        final Consumer consumer = consumerService.find(reviewDto.getAuthorId());
        if (consumer == null)
            throw NotFoundException.create("Consumer", reviewDto.getAuthorId());

        ModelMapper modelMapper = new ModelMapper();
        // Due to Consumer is abstract
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(new PropertyMap<RequestCreateReviewDto, Review>() {
            @Override
            protected void configure() {
                skip(destination.getAuthor());
            }
        });

        Review review = modelMapper.map(reviewDto, Review.class);
        review.setContract(contract);
        review.setAuthor(consumer);
        reviewService.update(review);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", review.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProperty(@PathVariable Integer id) {
        final Review toRemove = reviewService.find(id);
        if (toRemove == null) {
            return;
        }
        reviewService.remove(toRemove);
        LOG.debug("Removed property {}.", toRemove);
    }
}
