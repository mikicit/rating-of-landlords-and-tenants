package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.request.TenantCreateRequestDTO;
import dev.mikita.rolt.dto.request.TenantUpdateRequestDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.dto.response.PropertyResponseDTO;
import dev.mikita.rolt.dto.response.TenantResponseDTO;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.model.*;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.model.mapper.PropertyMapper;
import dev.mikita.rolt.model.mapper.TenantMapper;
import dev.mikita.rolt.repository.PropertyRepository;
import dev.mikita.rolt.repository.TenantRepository;
import dev.mikita.rolt.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * The type Tenant service.
 */
@Service
@Transactional
public class TenantService {
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final TenantMapper tenantMapper;
    private final PropertyMapper propertyMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TenantService(TenantRepository tenantRepository,
                         UserRepository userRepository,
                         PropertyRepository propertyRepository,
                         TenantMapper tenantMapper,
                         PropertyMapper propertyMapper,
                         PasswordEncoder passwordEncoder) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.tenantMapper = tenantMapper;
        this.propertyMapper = propertyMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @param filters  the filters
     * @return the page
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<TenantResponseDTO> getAll(Pageable pageable, Map<String, Object> filters) {
        Specification<Tenant> spec = Specification.where(null);

        if (filters.containsKey("status")) {
            ConsumerStatus status = ConsumerStatus.valueOf((String) filters.get("status"));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (filters.containsKey("gender")) {
            ConsumerGender gender = ConsumerGender.valueOf((String) filters.get("gender"));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("gender"), gender));
        }

        Page<Tenant> page = tenantRepository.findAll(spec, pageable);
        List<TenantResponseDTO> content = page.getContent().stream()
                .map(tenantMapper::toTenantResponseDTO)
                .toList();

        return new PagedResponseDTO<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    /**
     * Find tenant.
     *
     * @param id the id
     * @return the tenant
     */
    @Transactional(readOnly = true)
    public TenantResponseDTO get(Long id) {
        return tenantMapper.toTenantResponseDTO(tenantRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Tenant.class.getSimpleName(), id)));
    }

    public void add(TenantCreateRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ValidationException("User with email " + dto.email() + " already exists");
        }

        Tenant tenant = tenantMapper.toTenant(dto);
        tenant.encodePassword(passwordEncoder);

        tenantRepository.save(tenant);
    }

    public void update(Long id, TenantUpdateRequestDTO dto) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Tenant.class.getSimpleName(), id));

        tenantMapper.updateTenantFromDto(dto, tenant);
    }

    public void remove(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Tenant.class.getSimpleName(), id));

        tenant.setInSearch(false);
        tenant.setStatus(ConsumerStatus.DELETED);
    }

    public List<PropertyResponseDTO> getFavorites(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> NotFoundException.create(Tenant.class.getSimpleName(), tenantId));

        return tenant.getFavorites().stream().map(propertyMapper::toPropertyResponseDTO).toList();
    }

    public void addFavorite(Long tenantId, Long propertyId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> NotFoundException.create(Tenant.class.getSimpleName(), tenantId));

        Property property = propertyRepository.getReferenceById(propertyId);
        tenant.addFavorite(property);
        tenantRepository.save(tenant);
    }

    public void removeFavorite(Long tenantId, Long propertyId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> NotFoundException.create(Tenant.class.getSimpleName(), tenantId));

        Property property = propertyRepository.getReferenceById(propertyId);
        tenant.removeFavorite(property);
        tenantRepository.save(tenant);
    }
}
