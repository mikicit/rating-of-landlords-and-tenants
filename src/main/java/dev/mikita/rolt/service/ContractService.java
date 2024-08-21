package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.request.ContractCreateRequestDTO;
import dev.mikita.rolt.dto.request.ContractUpdateRequestDTO;
import dev.mikita.rolt.dto.response.ContractResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.model.Contract;
import dev.mikita.rolt.model.mapper.ContractMapper;
import dev.mikita.rolt.repository.*;
import dev.mikita.rolt.repository.specification.ContractSpecs;
import dev.mikita.rolt.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The type Contract service.
 */
@Service
@Transactional
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final LandlordRepository landlordRepository;
    private final TenantRepository tenantRepository;
    private final PropertyRepository propertyRepository;
    private final ContractMapper contractMapper;

    @Autowired
    public ContractService(ContractRepository contractRepository,
                           UserRepository userRepository,
                           LandlordRepository landlordRepository,
                           TenantRepository tenantRepository,
                           PropertyRepository propertyRepository,
                           ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
        this.landlordRepository = landlordRepository;
        this.tenantRepository = tenantRepository;
        this.propertyRepository = propertyRepository;
        this.contractMapper = contractMapper;
    }

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @param filters  the filters
     * @return the page
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<ContractResponseDTO> getAll(Pageable pageable, Map<String, Object> filters) {
        Specification<Contract> specs = Specification.where(null);

        if (filters.containsKey("landlordId")) {
            specs = specs.and(ContractSpecs
                    .withLandlord(landlordRepository.getReferenceById((Long) filters.get("landlordId"))));
        }

        if (filters.containsKey("tenantId")) {
            specs = specs.and(ContractSpecs
                    .withTenant(tenantRepository.getReferenceById((Long) filters.get("tenantId"))));
        }

        if (filters.containsKey("propertyId")) {
            specs = specs.and(ContractSpecs
                    .withProperty(propertyRepository.getReferenceById((Long) filters.get("propertyId"))));
        }

        if (filters.containsKey("fromDate")) {
            specs = specs.and(ContractSpecs.withStartDateAfter((LocalDate) filters.get("fromDate")));
        }

        if (filters.containsKey("toDate")) {
            specs = specs.and(ContractSpecs.withEndDateBefore((LocalDate) filters.get("toDate")));
        }

        Page<Contract> result = contractRepository.findAll(specs, pageable);
        List<ContractResponseDTO> contracts = result.stream().map(contractMapper::toContractResponseDTO).toList();

        return new PagedResponseDTO<>(
                contracts,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public ContractResponseDTO get(Long id) {
        return contractMapper.toContractResponseDTO(contractRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Contract.class.getSimpleName(), id)));
    }

    public ContractResponseDTO add(ContractCreateRequestDTO contractDTO, Principal principal) {
        List<Contract> intersections = contractRepository.findIntersectionsByDateRange(contractDTO.propertyId(),
                contractDTO.startDate(), contractDTO.endDate());

        if (!intersections.isEmpty()) {
            throw new ValidationException("Contracts already exist in this date range.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Contract contract = contractMapper.toContract(contractDTO);
        contract.setTenant(tenantRepository.getReferenceById(userDetails.getUser().getId()));
        return contractMapper.toContractResponseDTO(contractRepository.save(contract));
    }

    public void update(ContractUpdateRequestDTO contractDTO) {
        contractRepository.save(contractMapper.toContract(contractDTO));
    }

    public void remove(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Contract.class.getSimpleName(), id));

        contractRepository.delete(contract);
    }
}
