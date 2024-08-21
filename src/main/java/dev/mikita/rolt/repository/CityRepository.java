package dev.mikita.rolt.repository;

import dev.mikita.rolt.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CityRepository extends JpaRepository<City, Long> {
    Page<City> findAll(Pageable pageable, String name);
}
