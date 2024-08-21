package dev.mikita.rolt.repository;

import dev.mikita.rolt.model.Moderator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ModeratorRepository extends JpaRepository<Moderator, Long> {
    Page<Moderator> findAll(Pageable pageable, String name);
}
