package dev.mikita.rolt.service;

import dev.mikita.rolt.dto.response.PagedResponseDTO;
import dev.mikita.rolt.dto.response.UserResponseDTO;
import dev.mikita.rolt.exception.NotFoundException;
import dev.mikita.rolt.model.User;
import dev.mikita.rolt.model.mapper.UserMapper;
import dev.mikita.rolt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * The type User service.
 */
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<UserResponseDTO> getAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        List<UserResponseDTO> users = page.getContent().stream()
                .map(userMapper::toUserResponseDTO)
                .toList();

        return new PagedResponseDTO<>(
                users,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    /**
     * Find user.
     *
     * @param id the id
     * @return the user
     */
    @Transactional(readOnly = true)
    public UserResponseDTO get(Long id) {
        return userMapper.toUserResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create(User.class.getSimpleName(), id)));
    }
}
