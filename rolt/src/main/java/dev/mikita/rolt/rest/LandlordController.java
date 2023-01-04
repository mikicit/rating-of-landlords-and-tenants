package dev.mikita.rolt.rest;

import dev.mikita.rolt.entity.Contract;
import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.entity.Role;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.security.model.AuthenticationToken;
import dev.mikita.rolt.service.ContractService;
import dev.mikita.rolt.service.LandlordService;
import dev.mikita.rolt.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/v1/landlords")
public class LandlordController {
    private static final Logger LOG = LoggerFactory.getLogger(LandlordController.class);

    private final LandlordService landlordService;
    private final PropertyService propertyService;
    private final ContractService contractService;


    @Autowired
    public LandlordController(LandlordService landlordService, PropertyService propertyService, ContractService contractService) {
        this.landlordService = landlordService;
        this.propertyService = propertyService;
        this.contractService = contractService;
    }

//    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createLandlord(@RequestBody Landlord landlord) {
        landlordService.persist(landlord);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Landlord getLandlord(@PathVariable Integer id) {
        final Landlord landlord = landlordService.find(id);
        if (landlord == null)
            throw NotFoundException.create("Landlord", id);
        return landlord;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Landlord> getLandlords() {
        return landlordService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLandlord(Principal principal, @PathVariable Integer id, @RequestBody Landlord landlord) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        if ((auth.getPrincipal().getUser().getRole() != Role.ADMIN
                || auth.getPrincipal().getUser().getRole() != Role.MODERATOR) &&
                !auth.getPrincipal().getUser().getId().equals(id)) {
            throw new AccessDeniedException("Cannot update another landlord.");
        }

        final Landlord original = getLandlord(id);
        if (!original.getId().equals(landlord.getId())) {
            throw new ValidationException("Landlord identifier in the data does not match the one in the request URL.");
        }

        landlordService.update(landlord);
        LOG.debug("Updated landlord {}.", landlord);
    }

    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLandlord(Principal principal, @PathVariable Integer id) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        if ((auth.getPrincipal().getUser().getRole() != Role.ADMIN
                || auth.getPrincipal().getUser().getRole() != Role.MODERATOR) &&
                !auth.getPrincipal().getUser().getId().equals(id)) {
            throw new AccessDeniedException("Cannot delete another landlord.");
        }

        final Landlord toRemove = landlordService.find(id);
        if (toRemove == null) {
            return;
        }

        landlordService.remove(toRemove);
        LOG.debug("Removed landlord {}.", toRemove);
    }

    @GetMapping(value = "/{id}/properties", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Property> getProperties(@PathVariable Integer id) {
        return propertyService.findAllPublished(getLandlord(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Contract> getContracts(Principal principal, @PathVariable Integer id) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        if ((auth.getPrincipal().getUser().getRole() != Role.ADMIN
                || auth.getPrincipal().getUser().getRole() != Role.MODERATOR)
                && !auth.getPrincipal().getUser().getId().equals(id)) {
            throw new AccessDeniedException("Cannot view another landlord's contracts.");
        }

        return contractService.findByUser(getLandlord(id));
    }
}
