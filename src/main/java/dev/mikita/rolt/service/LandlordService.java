package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.request.LandlordCreateRequestDTO;
import dev.mikita.rolt.dto.request.LandlordUpdateRequestDTO;
import dev.mikita.rolt.dto.response.LandlordResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.model.*;
import dev.mikita.rolt.model.mapper.LandlordMapper;
import dev.mikita.rolt.repository.LandlordRepository;
import dev.mikita.rolt.repository.UserRepository;
import dev.mikita.rolt.repository.specification.LandlordSpecs;
import dev.mikita.rolt.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Landlord service.
 */
@Service
@Transactional
public class LandlordService {
    private final LandlordRepository landlordRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LandlordMapper landlordMapper;

    @Autowired
    public LandlordService(LandlordRepository landlordRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder, LandlordMapper landlordMapper) {
        this.landlordRepository = landlordRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.landlordMapper = landlordMapper;
    }

    @Transactional(readOnly = true)
    public PagedResponseDTO<LandlordResponseDTO> getAll(Pageable pageable, Map<String, Object> filters) {
        Specification<Landlord> spec = Specification.where(null);

        if (filters.containsKey("status")) {
            ConsumerStatus status = ConsumerStatus.valueOf((String) filters.get("status"));
            spec = spec.and(LandlordSpecs.withStatus(status));
        }

        if (filters.containsKey("gender")) {
            ConsumerGender gender = ConsumerGender.valueOf((String) filters.get("gender"));
            spec = spec.and(LandlordSpecs.withGender(gender));
        }

        Page<Landlord> page = landlordRepository.findAll(spec, pageable);
        List<LandlordResponseDTO> content = page.getContent().stream()
                .map(landlordMapper::toLandlordResponseDTO)
                .toList();

        return new PagedResponseDTO<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Transactional(readOnly = true)
    public LandlordResponseDTO get(Long id) {
        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Landlord.class.getSimpleName(), id));

        if (landlord.getStatus() != ConsumerStatus.ACTIVE) {
            throw NotFoundException.create(Landlord.class.getSimpleName(), id);
        }

        return landlordMapper.toLandlordResponseDTO(landlord);
    }

    public void add(LandlordCreateRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ValidationException("User with email " + dto.email() + " already exists");
        }

        Landlord landlord = landlordMapper.toLandlord(dto);
        landlord.encodePassword(passwordEncoder);

        landlordRepository.save(landlord);
    }

    public LandlordResponseDTO update(Long id, LandlordUpdateRequestDTO dto, Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        User user = userDetails.getUser();

        if (!Objects.equals(id, user.getId())) {
            throw new ValidationException("You can only update your own profile.");
        }

        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Landlord.class.getSimpleName(), id));

        if (landlord.getStatus() != ConsumerStatus.ACTIVE) {
            throw NotFoundException.create(Landlord.class.getSimpleName(), id);
        }

        landlordMapper.updateLandlordFromDTO(dto, landlord);
        return landlordMapper.toLandlordResponseDTO(landlordRepository.save(landlord));
    }
}
