package dev.mikita.rolt.controller;

import dev.mikita.rolt.dto.request.LandlordCreateRequestDTO;
import dev.mikita.rolt.dto.request.LandlordUpdateRequestDTO;
import dev.mikita.rolt.dto.response.LandlordResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.dto.response.PropertyResponseDTO;
import dev.mikita.rolt.controller.util.RestUtils;
import dev.mikita.rolt.model.*;
import dev.mikita.rolt.service.ConsumerService;
import dev.mikita.rolt.service.LandlordService;
import dev.mikita.rolt.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/landlords")
public class LandlordController {
    private final LandlordService landlordService;
    private final PropertyService propertyService;
    private final ConsumerService consumerService;

    @Autowired
    public LandlordController(LandlordService landlordService,
                              PropertyService propertyService,
                              ConsumerService consumerService) {
        this.landlordService = landlordService;
        this.propertyService = propertyService;
        this.consumerService = consumerService;
    }

    @PreAuthorize("(anonymous)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createLandlord(@RequestBody @Valid LandlordCreateRequestDTO landlordDto) {
        landlordService.add(landlordDto);
        HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LandlordResponseDTO> getLandlord(@PathVariable Long id) {
        return ResponseEntity.ok(landlordService.get(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponseDTO<LandlordResponseDTO>> getLandlords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ConsumerGender gender) {

        // Filters
        Map<String, Object> filters = new HashMap<>();
        if (gender != null) filters.put("gender", gender);
        filters.put("status", ConsumerStatus.ACTIVE);

        return ResponseEntity.ok(landlordService.getAll(PageRequest.of(page, size), filters));
    }

    @PreAuthorize("hasRole('ROLE_LANDLORD')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateLandlord(
            @PathVariable Long id, @RequestBody @Valid LandlordUpdateRequestDTO landlordDto, Principal principal) {
        landlordService.update(id, landlordDto, principal);
        HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", id);
        return ResponseEntity.noContent().headers(headers).build();
    }

    @PreAuthorize("hasRole('ROLE_LANDLORD')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteLandlord(@PathVariable Long id, Principal principal) {
        consumerService.removeByConsumer(id, principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/properties", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponseDTO<PropertyResponseDTO>> getProperties(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) Double minSquare,
            @RequestParam(required = false) Double maxSquare,
            @RequestParam(required = false) Boolean isAvailable) {

        // Filters
        Map<String, Object> filters = new HashMap<>();
        if (cityId != null) filters.put("cityId", cityId);
        if (propertyType != null) filters.put("propertyType", propertyType);
        if (minSquare != null) filters.put("minSquare", minSquare);
        if (maxSquare != null) filters.put("maxSquare", maxSquare);
        if (isAvailable != null) filters.put("isAvailable", isAvailable);
        filters.put("status", PublicationStatus.PUBLISHED);
        filters.put("ownerId", id);

        return ResponseEntity.ok(propertyService.getAll(PageRequest.of(page, size), filters));
    }
}
