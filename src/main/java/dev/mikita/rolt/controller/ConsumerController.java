package dev.mikita.rolt.controller;

import dev.mikita.rolt.dto.response.ContractResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.dto.response.ReviewResponseDTO;
import dev.mikita.rolt.model.*;
import dev.mikita.rolt.service.ConsumerService;
import dev.mikita.rolt.service.ContractService;
import dev.mikita.rolt.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/consumers")
public class ConsumerController {
    private final ConsumerService consumerService;
    private final ReviewService reviewService;
    private final ContractService contractService;

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
    public ResponseEntity<PagedResponseDTO<ReviewResponseDTO>> getReviews(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Filters
        Map<String, Object> filters = new HashMap<>();
        filters.put("status", PublicationStatus.PUBLISHED);
        filters.put("reviewedId", id);

        return ResponseEntity.ok(reviewService.getAll(PageRequest.of(page, size), filters));
    }

    @GetMapping(value = "/{id}/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponseDTO<ContractResponseDTO>> getContracts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        // Filters
        Map<String, Object> filters = new HashMap<>();
        if (fromDate != null) filters.put("fromDate", fromDate);
        if (toDate != null) filters.put("toDate", toDate);

        return ResponseEntity.ok(contractService.getAll(id, PageRequest.of(page, size), filters));
    }

    @GetMapping(value = "/{id}/rating", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getRating(@PathVariable Long id) {
        return ResponseEntity.ok(consumerService.getRating(id));
    }
}
