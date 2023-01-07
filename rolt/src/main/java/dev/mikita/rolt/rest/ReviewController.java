package dev.mikita.rolt.rest;

import dev.mikita.rolt.dto.review.RequestCreateReviewDto;
import dev.mikita.rolt.dto.review.ResponsePublicReviewDto;
import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.service.ConsumerService;
import dev.mikita.rolt.service.ContractService;
import dev.mikita.rolt.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
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
    public List<ResponsePublicReviewDto> getReviews() {
        ModelMapper modelMapper = new ModelMapper();

        return reviewService.findAll().stream()
                .map(review -> modelMapper.map(review, ResponsePublicReviewDto.class))
                .collect(Collectors.toList());
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
}
