package dev.mikita.rolt.rest;

import dev.mikita.rolt.entity.Contract;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.entity.Role;
import dev.mikita.rolt.entity.Tenant;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.security.model.AuthenticationToken;
import dev.mikita.rolt.service.ContractService;
import dev.mikita.rolt.service.PropertyService;
import dev.mikita.rolt.service.TenantService;
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
@RequestMapping("/rest/v1/tenants")
public class TenantController {
    private static final Logger LOG = LoggerFactory.getLogger(TenantController.class);

    private final TenantService tenantService;
    private final PropertyService propertyService;
    private final ContractService contractService;

    @Autowired
    public TenantController(TenantService tenantService,
                            PropertyService propertyService,
                            ContractService contractService) {
        this.tenantService = tenantService;
        this.propertyService = propertyService;
        this.contractService = contractService;
    }

//    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_GUEST', 'ROLE_TENANT', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody Tenant tenant) {
        tenantService.persist(tenant);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Tenant> getTenants() {
        return tenantService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tenant getTenant(@PathVariable Integer id) {
        final Tenant tenant = tenantService.find(id);
        if (tenant == null)
            throw NotFoundException.create("Tenant", id);
        return tenant;
    }

    @PreAuthorize("hasAnyRole('ROLE_TENANT', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTenant(Principal principal, @PathVariable Integer id, @RequestBody Tenant tenant) {
        final Tenant original = getTenant(id);
        final AuthenticationToken auth = (AuthenticationToken) principal;
        if ((auth.getPrincipal().getUser().getRole() != Role.ADMIN
                || auth.getPrincipal().getUser().getRole() != Role.MODERATOR) &&
                !auth.getPrincipal().getUser().getId().equals(id)) {
            throw new AccessDeniedException("Cannot update another tenant.");
        }

        if (!original.getId().equals(tenant.getId())) {
            throw new ValidationException("Tenant identifier in the data does not match the one in the request URL.");
        }
        tenantService.update(tenant);
        LOG.debug("Updated tenant {}.", tenant);
    }

    @PreAuthorize("hasAnyRole('ROLE_TENANT', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTenant(Principal principal, @PathVariable Integer id) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        if ((auth.getPrincipal().getUser().getRole() != Role.ADMIN
                || auth.getPrincipal().getUser().getRole() != Role.MODERATOR)
                && !auth.getPrincipal().getUser().getId().equals(id)) {
            throw new AccessDeniedException("Cannot delete another tenant.");
        }

        final Tenant toRemove = tenantService.find(id);
        if (toRemove == null) {
            return;
        }

        tenantService.remove(toRemove);
        LOG.debug("Removed tenant {}.", toRemove);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_TENANT')")
    @GetMapping(value = "/{id}/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Property> getFavorites(Principal principal, @PathVariable Integer id) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        if ((auth.getPrincipal().getUser().getRole() != Role.ADMIN
                || auth.getPrincipal().getUser().getRole() != Role.MODERATOR) &&
                !auth.getPrincipal().getUser().getId().equals(id)) {
            throw new AccessDeniedException("Cannot access favorites of another tenant.");
        }

        return tenantService.getFavorites(getTenant(id));
    }

    @PreAuthorize("hasRole('ROLE_TENANT')")
    @PutMapping(value = "/{user_id}/favorites/{property_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFavorite(Principal principal, @PathVariable Integer user_id, @PathVariable Integer property_id) {
        final AuthenticationToken auth = (AuthenticationToken) principal;

        if (!auth.getPrincipal().getUser().getId().equals(user_id)) {
            throw new AccessDeniedException("Cannot access favorites of another tenant.");
        }

        Property property = propertyService.find(property_id);

        if (property == null) {
            throw NotFoundException.create("Property", property_id);
        }

        Tenant tenant = getTenant(user_id);

        tenantService.addFavorite(property, tenant);
    }

    @PreAuthorize("hasRole('ROLE_TENANT')")
    @DeleteMapping(value = "/{user_id}/favorites/{property_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeFavorite(Principal principal, @PathVariable Integer user_id, @PathVariable Integer property_id) {
        final AuthenticationToken auth = (AuthenticationToken) principal;

        if (!auth.getPrincipal().getUser().getId().equals(user_id)) {
            throw new AccessDeniedException("Cannot access favorites of another tenant.");
        }

        Property property = propertyService.find(property_id);

        if (property == null) {
            throw NotFoundException.create("Property", property_id);
        }

        Tenant tenant = getTenant(user_id);

        tenantService.addFavorite(property, tenant);
    }

    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Contract> getContracts(Principal principal, @PathVariable Integer id) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        if ((auth.getPrincipal().getUser().getRole() != Role.ADMIN
                || auth.getPrincipal().getUser().getRole() != Role.MODERATOR)
                && !auth.getPrincipal().getUser().getId().equals(id)) {
            throw new AccessDeniedException("Cannot view another tenant's contracts.");
        }

        return contractService.findByUser(getTenant(id));
    }
}
