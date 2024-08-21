package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.request.ModeratorCreateRequestDTO;
import dev.mikita.rolt.dto.request.ModeratorUpdateRequestDTO;
import dev.mikita.rolt.dto.response.ModeratorResponseDTO;
import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.model.Moderator;
import dev.mikita.rolt.exception.ValidationException;
import dev.mikita.rolt.model.mapper.ModeratorMapper;
import dev.mikita.rolt.repository.ModeratorRepository;
import dev.mikita.rolt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * The type Moderator service.
 */
@Service
@Transactional
public class ModeratorService {
    private final ModeratorRepository moderatorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModeratorMapper moderatorMapper;

    @Autowired
    public ModeratorService(ModeratorRepository moderatorRepository,
                            UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            ModeratorMapper moderatorMapper) {
        this.moderatorRepository = moderatorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.moderatorMapper = moderatorMapper;
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<ModeratorResponseDTO> getAll(Pageable pageable) {
        Page<Moderator> page = moderatorRepository.findAll(pageable);
        List<ModeratorResponseDTO> content = page.getContent().stream()
                .map(moderatorMapper::toModeratorResponseDTO)
                .toList();

        return new PagedResponseDTO<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    /**
     * Find moderator.
     *
     * @param id the id
     * @return the moderator
     */
    @Transactional(readOnly = true)
    public ModeratorResponseDTO get(Long id) {
        return moderatorMapper.toModeratorResponseDTO(moderatorRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Moderator.class.getSimpleName(), id)));
    }

    public ModeratorResponseDTO add(ModeratorCreateRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ValidationException("User with email " + dto.email() + " already exists");
        }

        Moderator moderator = moderatorMapper.toModerator(dto);
        moderator.encodePassword(passwordEncoder);

        return moderatorMapper.toModeratorResponseDTO(moderatorRepository.save(moderator));
    }

    public ModeratorResponseDTO update(Long id, ModeratorUpdateRequestDTO dto) {
        Moderator moderator = moderatorRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Moderator.class.getSimpleName(), id));

        moderatorMapper.updateModeratorFromDTO(dto, moderator);
        return moderatorMapper.toModeratorResponseDTO(moderatorRepository.save(moderator));
    }

    public void remove(Long id) {
        Moderator moderator = moderatorRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(Moderator.class.getSimpleName(), id));

        moderatorRepository.delete(moderator);
    }
}
