package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.request.PropertyCreateRequestDTO;
import dev.mikita.rolt.dto.request.PropertyUpdateRequestDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.dto.response.PropertyResponseDTO;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.model.Landlord;
import dev.mikita.rolt.model.Property;
import dev.mikita.rolt.model.PropertyType;
import dev.mikita.rolt.model.PublicationStatus;
import dev.mikita.rolt.model.mapper.PropertyMapper;
import dev.mikita.rolt.repository.CityRepository;
import dev.mikita.rolt.repository.PropertyRepository;
import dev.mikita.rolt.repository.specification.PropertySpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * The type Property service.
 */
@Service
@Transactional
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository,
                           CityRepository cityRepository,
                           PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.cityRepository = cityRepository;
        this.propertyMapper = propertyMapper;
    }

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @param filters  the filters
     * @return the page
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<PropertyResponseDTO> getAll(Pageable pageable, Map<String, Object> filters) {
        Specification<Property> spec = Specification.where(null);

        if (filters.containsKey("cityId")) {
            spec = spec.and(PropertySpecs.withCity(cityRepository.getReferenceById((Long) filters.get("cityId"))));
        }

        if (filters.containsKey("type")) {
            spec = spec.and(PropertySpecs.withPropertyType(PropertyType.valueOf(filters.get("type").toString())));
        }

        if (filters.containsKey("minSquare")) {
            spec = spec.and(PropertySpecs.withMinSquare(Double.parseDouble(filters.get("minSquare").toString())));
        }

        if (filters.containsKey("maxSquare")) {
            spec = spec.and(PropertySpecs.withMaxSquare(Double.parseDouble(filters.get("maxSquare").toString())));
        }

        if (filters.containsKey("isAvailable")) {
            spec = spec.and(PropertySpecs.withAvailability(Boolean.parseBoolean(filters.get("isAvailable").toString())));
        }

        Page<Property> result = propertyRepository.findAll(spec, pageable);
        List<PropertyResponseDTO> properties = result.stream().map(propertyMapper::toPropertyResponseDTO).toList();

        return new PagedResponseDTO<>(
                properties,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    /**
     * Find property.
     *
     * @param id the id
     * @return the property
     */
    @Transactional(readOnly = true)
    public PropertyResponseDTO get(Long id) {
        return propertyMapper.toPropertyResponseDTO(propertyRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Property.class.getSimpleName(), id)));
    }

    public void add(PropertyCreateRequestDTO dto, Principal principal) {
        Landlord landlord = (Landlord) principal;
        Property property = propertyMapper.toProperty(dto);
        property.setOwner(landlord);
        propertyRepository.save(property);
    }

    public void update(Long id, PropertyUpdateRequestDTO dto) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Property.class.getSimpleName(), id));

        propertyMapper.updatePropertyFromDto(dto, property);
    }

    public void remove(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Property.class.getSimpleName(), id));

        property.setAvailable(false);
        property.setStatus(PublicationStatus.DELETED);
    }

    public void publish(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Property.class.getSimpleName(), id));

        property.setStatus(PublicationStatus.PUBLISHED);
    }

    public void moderate(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Property.class.getSimpleName(), id));

        property.setStatus(PublicationStatus.MODERATION);
    }
}
