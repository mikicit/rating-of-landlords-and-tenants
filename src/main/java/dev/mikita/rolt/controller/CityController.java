package dev.mikita.rolt.controller;

import dev.mikita.rolt.dto.request.CityUpdateRequestDTO;
import dev.mikita.rolt.dto.response.CityResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.controller.util.RestUtils;
import dev.mikita.rolt.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponseDTO<CityResponseDTO>> getCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")));
        PagedResponseDTO<CityResponseDTO> response = cityService.getAll(pageable, name);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CityResponseDTO> getCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.get(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addCity(@RequestBody CityUpdateRequestDTO cityUpdateRequestDTO) {
        CityResponseDTO city = cityService.add(cityUpdateRequestDTO);
        return ResponseEntity.created(RestUtils.createLocationUriFromCurrentUri(city.id())).build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCity(@PathVariable Long id, @RequestBody CityUpdateRequestDTO cityUpdateRequestDTO) {
        CityResponseDTO city = cityService.update(id, cityUpdateRequestDTO);
        return ResponseEntity.noContent().location(RestUtils.createLocationUriFromCurrentUri(city.id())).build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
