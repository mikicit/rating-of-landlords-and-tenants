package dev.mikita.rolt.rest;

import dev.mikita.rolt.dto.contract.RequestCreateContractDto;
import dev.mikita.rolt.dto.contract.RequestUpdateContractDto;
import dev.mikita.rolt.dto.contract.ResponsePublicContractDto;
import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.security.model.CustomUserDetails;
import dev.mikita.rolt.service.ContractService;
import dev.mikita.rolt.service.PropertyService;
import dev.mikita.rolt.service.TenantService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/contracts")
public class ContractController {
    private static final Logger LOG = LoggerFactory.getLogger(ContractController.class);

    private final ContractService contractService;
    private final PropertyService propertyService;
    private final TenantService tenantService;

    public ContractController(ContractService contractService,
                              PropertyService propertyService,
                              TenantService tenantService) {
        this.contractService = contractService;
        this.propertyService = propertyService;
        this.tenantService = tenantService;
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getContracts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        ModelMapper modelMapper = new ModelMapper();

        // Filters
        Map<String, Object> filters = new HashMap<>();
        if (fromDate != null) filters.put("fromDate", fromDate);
        if (toDate != null) filters.put("toDate", toDate);

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

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_TENANT', 'ROLE_MODERATOR')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponsePublicContractDto getContract(Principal principal, @PathVariable Integer id) {
        final Contract contract = contractService.find(id);

        if (contract == null) {
            throw NotFoundException.create("Contract", id);
        }

//        final CustomUserDetails userDetails = (CustomUserDetails) principal;
//        final User user = userDetails.getUser();
//
//        if ((user.getRole() != Role.ADMIN || user.getRole() != Role.MODERATOR)
//            && (!contract.getTenant().getId().equals(user.getId())
//            || !contract.getProperty().getOwner().getId().equals(user.getId()))) {
//            throw new AccessDeniedException("Cannot access contract of another customer.");
//        }

        return new ModelMapper().map(contract, ResponsePublicContractDto.class);
    }

//    @PreAuthorize("hasRole('ROLE_TENANT')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createContract(@RequestBody @Valid RequestCreateContractDto contractDto) {

//        // TODO Compare tenant's id with tenant's id in contractDto
//        if (!original.getId().equals(contractDto.getTenantId())) {
//            throw new ValidationException("You cannot create contracts for other users.");
//        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        final Property property = propertyService.find(contractDto.getPropertyId());
        if (property == null)
            throw NotFoundException.create("Property", contractDto.getPropertyId());

        final Tenant tenant = tenantService.find(contractDto.getTenantId());
        if (tenant == null)
            throw NotFoundException.create("Tenant", contractDto.getTenantId());

        Contract contract = modelMapper.map(contractDto, Contract.class);
        contract.setProperty(property);
        contract.setTenant(tenant);
        contractService.persist(modelMapper.map(contractDto, Contract.class));

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", contract.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    //    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_MODERATOR')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContract(@PathVariable Integer id, @RequestBody @Valid RequestUpdateContractDto contractDto) {
        ModelMapper modelMapper = new ModelMapper();

        final Contract original = contractService.find(id);
        if (original == null) {
            throw NotFoundException.create("Contract", id);
        }

        if (!original.getId().equals(contractDto.getId())) {
            throw new ValidationException("Contract identifier in the data does not match the one in the request URL.");
        }

        final Property property = propertyService.find(contractDto.getPropertyId());
        if (property == null)
            throw NotFoundException.create("Property", contractDto.getPropertyId());

        final Tenant tenant = tenantService.find(contractDto.getTenantId());
        if (tenant == null)
            throw NotFoundException.create("Tenant", contractDto.getTenantId());

        Contract contract = modelMapper.map(contractDto, Contract.class);
        contract.setProperty(property);
        contract.setTenant(tenant);
        contractService.update(contract);

        LOG.debug("Updated property {}.", contract);
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContract(Principal principal, @PathVariable Integer id) {
        final Contract toRemove = contractService.find(id);
        if (toRemove == null) {
            return;
        }
        contractService.remove(toRemove);
        LOG.debug("Removed property {}.", toRemove);
    }
}
