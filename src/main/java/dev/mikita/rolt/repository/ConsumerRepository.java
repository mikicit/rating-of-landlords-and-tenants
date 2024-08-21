package dev.mikita.rolt.repository;

import dev.mikita.rolt.model.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
}
