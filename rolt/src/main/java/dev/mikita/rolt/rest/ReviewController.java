package dev.mikita.rolt.rest;

import dev.mikita.rolt.entity.Review;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rest/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review getReview(@PathVariable Integer id) {
        final Review review = reviewService.find(id);
        if (review == null)
            throw NotFoundException.create("Review", id);
        return review;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Review> getReviews() {
        return reviewService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_TENANT')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addReview(@RequestBody Review review) {
        reviewService.persist(review);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", review.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
