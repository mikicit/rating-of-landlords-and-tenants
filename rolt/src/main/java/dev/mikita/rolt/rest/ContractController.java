package dev.mikita.rolt.rest;

import dev.mikita.rolt.entity.Contract;
import dev.mikita.rolt.entity.Role;
import dev.mikita.rolt.entity.User;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.security.model.AuthenticationToken;
import dev.mikita.rolt.service.ContractService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/v1/contracts")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Contract> getContracts() {
        return contractService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_TENANT', 'ROLE_MODERATOR')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Contract getContract(Principal principal, @PathVariable Integer id) {
        final Contract contract = contractService.find(id);

        if (contract == null) {
            throw NotFoundException.create("Contract", id);
        }

        final AuthenticationToken auth = (AuthenticationToken) principal;
        User user = auth.getPrincipal().getUser();

        if ((user.getRole() != Role.ADMIN || user.getRole() != Role.MODERATOR)
            && (!contract.getTenant().getId().equals(user.getId())
            || !contract.getProperty().getOwner().getId().equals(user.getId()))) {
            throw new AccessDeniedException("Cannot access contract of another customer.");
        }

        return contract;
    }

    @PreAuthorize("hasRole('ROLE_TENANT')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createContract(@RequestBody Contract contract) {
        contractService.persist(contract);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", contract.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
