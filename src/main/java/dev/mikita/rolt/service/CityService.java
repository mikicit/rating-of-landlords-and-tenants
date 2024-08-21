package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.request.CityUpdateRequestDTO;
import dev.mikita.rolt.dto.response.CityResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.model.City;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.model.mapper.CityMapper;
import dev.mikita.rolt.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * The type City service.
 */
@Service
@Transactional
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Autowired
    public CityService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    @Transactional(readOnly = true)
    public PagedResponseDTO<CityResponseDTO> getAll(Pageable pageable, String name) {
        Page<City> result = cityRepository.findAll(pageable, name);
        List<CityResponseDTO> cities = result.getContent().stream()
                .map(cityMapper::toCityResponseDTO)
                .toList();

        return new PagedResponseDTO<>(
                cities,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public CityResponseDTO get(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(City.class.getSimpleName(), id));

        return cityMapper.toCityResponseDTO(city);
    }

    public CityResponseDTO add(CityUpdateRequestDTO dto) {
        return cityMapper.toCityResponseDTO(cityRepository.save(cityMapper.toCity(dto)));
    }

    public CityResponseDTO update(Long id, CityUpdateRequestDTO cityUpdateRequestDTO) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(City.class.getSimpleName(), id));
        cityMapper.updateCityFromDTO(cityUpdateRequestDTO, city);
        return cityMapper.toCityResponseDTO(cityRepository.save(city));
    }

    public void remove(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(City.class.getSimpleName(), id));
        cityRepository.delete(city);
    }
}
