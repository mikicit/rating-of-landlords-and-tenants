package dev.mikita.rolt.rest;

import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rest/v1/cities")
public class CityController {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyController.class);

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<City> getCities() {
        return cityService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public City getCity(@PathVariable Integer id) {
        final City city = cityService.find(id);

        if (city == null) {
            throw NotFoundException.create("City", id);
        }

        return city;
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCity(@RequestBody City city) {
        cityService.persist(city);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", city.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCity(@PathVariable Integer id) {
        final City toRemove = cityService.find(id);
        if (toRemove == null) {
            return;
        }
        cityService.remove(toRemove);
        LOG.debug("Removed city {}.", toRemove);
    }
}
