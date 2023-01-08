package dev.mikita.rolt.rest;

import dev.mikita.rolt.dto.contract.ResponsePublicContractDto;
import dev.mikita.rolt.dto.review.ResponsePublicReviewDto;
import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.service.ConsumerService;
import dev.mikita.rolt.service.ContractService;
import dev.mikita.rolt.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/consumers")
public class ConsumerController {
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerController.class);

    private ConsumerService consumerService;
    private ReviewService reviewService;
    private ContractService contractService;

    @Autowired
    public ConsumerController(
            ConsumerService consumerService,
            ReviewService reviewService,
            ContractService contractService) {
        this.consumerService = consumerService;
        this.reviewService = reviewService;
        this.contractService = contractService;
    }

    @GetMapping(value = "/{id}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getReviews(
            Principal principal,
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        final Consumer consumer = consumerService.find(id);
        if (consumer == null)
            throw NotFoundException.create("Consumer", id);

        ModelMapper modelMapper = new ModelMapper();

        // Filters
        Map<String, Object> filters = new HashMap<>();
        filters.put("status", PublicationStatus.PUBLISHED);
        filters.put("reviewedId", consumer.getId());

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

    //    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_TENANT', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getContracts(
            Principal principal,
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

//        final CustomUserDetails userDetails = (CustomUserDetails) principal;
//        final User user = userDetails.getUser();
//
//        if ((user.getRole() != Role.ADMIN
//                || user.getRole() != Role.MODERATOR)
//                && !user.getId().equals(id)) {
//            throw new AccessDeniedException("Cannot view another landlord's contracts.");
//        }

        final Consumer consumer = consumerService.find(id);
        if (consumer == null)
            throw NotFoundException.create("Consumer", id);

        ModelMapper modelMapper = new ModelMapper();

        // Filters
        Map<String, Object> filters = new HashMap<>();
        if (fromDate != null) filters.put("fromDate", fromDate);
        if (toDate != null) filters.put("toDate", toDate);

        if (consumer.getRole() == Role.LANDLORD) {
            filters.put("landlordId", id);
        } else if (consumer.getRole() == Role.TENANT) {
            filters.put("tenantId", id);
        }

        // Pagination and sorting
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> pageContracts = contractService.findAll(pageable, filters);
        List<Contract> contracts = pageContracts.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("contracts", contracts.stream()
                .map(contract -> modelMapper.map(contract, ResponsePublicContractDto.class))
                .collect(Collectors.toList()));
        response.put("currentPage", pageContracts.getNumber());
        response.put("totalItems", pageContracts.getTotalElements());
        response.put("totalPages", pageContracts.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
