package dev.mikita.rolt.rest;

import dev.mikita.rolt.dto.property.RequestCreatePropertyDto;
import dev.mikita.rolt.dto.property.RequestUpdatePropertyDto;
import dev.mikita.rolt.dto.property.ResponsePublicPropertyDto;
import dev.mikita.rolt.entity.City;
import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.rest.util.RestUtils;
import dev.mikita.rolt.service.CityService;
import dev.mikita.rolt.service.LandlordService;
import dev.mikita.rolt.service.PropertyService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/properties")
public class PropertyController {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyController.class);

    private final PropertyService propertyService;
    private final LandlordService landlordService;
    private final CityService cityService;

    @Autowired
    public PropertyController(PropertyService propertyService,
                              LandlordService landlordService,
                              CityService cityService) {
        this.propertyService = propertyService;
        this.landlordService = landlordService;
        this.cityService = cityService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResponsePublicPropertyDto> getProperties(@RequestParam(required = false) Boolean isAvailable) {
        ModelMapper modelMapper = new ModelMapper();
        List<Property> result;

        if (isAvailable == null) {
            result = propertyService.findAll();
        } else {
            result = propertyService.findAllAvailable();
        }

        return result.stream().map(property -> modelMapper.map(property, ResponsePublicPropertyDto.class)).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponsePublicPropertyDto getProperty(@PathVariable Integer id) {
        final Property property = propertyService.find(id);
        if (property == null) {
            throw NotFoundException.create("Property", id);
        }
        return new ModelMapper().map(property, ResponsePublicPropertyDto.class);
    }

//    @PreAuthorize("hasRole('ROLE_LANDLORD')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProperty(@RequestBody @Valid RequestCreatePropertyDto propertyDto) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        final Landlord landlord = landlordService.find(propertyDto.getOwnerId());
        if (landlord == null)
            throw NotFoundException.create("Landlord", propertyDto.getOwnerId());

        final City city = cityService.find(propertyDto.getCityId());
        if (city == null)
            throw NotFoundException.create("City", propertyDto.getCityId());

        Property property = modelMapper.map(propertyDto, Property.class);
        property.setOwner(landlord);
        property.setCity(city);
        propertyService.persist(property);

        LOG.debug("Created property {}.", property);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", property.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAnyRole('ROLE_LANDLORD', 'ROLE_LANDLORD', 'ROLE_MODERATOR')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProperty(@PathVariable Integer id, @RequestBody @Valid RequestUpdatePropertyDto propertyDto) {
        final Property original = propertyService.find(id);
        if (original == null) {
            throw NotFoundException.create("Property", id);
        }

        if (!original.getId().equals(propertyDto.getId())) {
            throw new ValidationException("Property identifier in the data does not match the one in the request URL.");
        }

        final City city = cityService.find(propertyDto.getCityId());
        if (city == null)
            throw NotFoundException.create("City", propertyDto.getCityId());

        Property property = new ModelMapper().map(propertyDto, Property.class);
        property.setCity(city);
        property.setOwner(original.getOwner());
        property.setStatus(original.getStatus());

        propertyService.update(property);

        LOG.debug("Updated property {}.", propertyDto);
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
