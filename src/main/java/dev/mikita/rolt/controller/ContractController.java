package dev.mikita.rolt.controller;

import dev.mikita.rolt.dto.request.ContractCreateRequestDTO;
import dev.mikita.rolt.dto.response.ContractResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.controller.util.RestUtils;
import dev.mikita.rolt.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponseDTO<ContractResponseDTO>> getContracts(
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

        return ResponseEntity.ok(contractService.getAll(PageRequest.of(page, size), filters));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContractResponseDTO> getContract(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.get(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_TENANT')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContractResponseDTO> createContract(
            @RequestBody @Valid ContractCreateRequestDTO contractDto, Principal principal) {
        ContractResponseDTO contract = contractService.add(contractDto, principal);
        return ResponseEntity.created(RestUtils.createLocationUriFromCurrentUri(contract.id())).build();
    }
}
