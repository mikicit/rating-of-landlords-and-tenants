package dev.mikita.rolt.rest;

import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.service.PropertyService;
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
@RequestMapping("/rest/v1/properties")
public class PropertyController {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyController.class);

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Property> getProperties(@RequestParam(required = false) Boolean isAvailable) {
        if (isAvailable == null)
            return propertyService.findAll();
        else
            return propertyService.findAllAvailable();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Property getProperty(@PathVariable Integer id) {
        final Property p = propertyService.find(id);
        if (p == null) {
            throw NotFoundException.create("Property", id);
        }
        return p;
    }

    @PreAuthorize("hasRole('ROLE_LANDLORD')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProperty(@RequestBody Property property) {
        propertyService.persist(property);
        LOG.debug("Created property {}.", property);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", property.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_MODERATOR')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProperty(@PathVariable Integer id, @RequestBody Property property) {
        final Property original = getProperty(id);
        if (!original.getId().equals(property.getId())) {
            throw new ValidationException("Property identifier in the data does not match the one in the request URL.");
        }
        propertyService.update(property);
        LOG.debug("Updated property {}.", property);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProperty(@PathVariable Integer id) {
        final Property toRemove = propertyService.find(id);
        if (toRemove == null) {
            return;
        }
        propertyService.remove(toRemove);
        LOG.debug("Removed property {}.", toRemove);
    }
}
