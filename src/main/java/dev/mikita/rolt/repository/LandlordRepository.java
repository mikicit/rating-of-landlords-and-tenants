package dev.mikita.rolt.repository;

import dev.mikita.rolt.model.Landlord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface LandlordRepository extends JpaRepository<Landlord, Long>, JpaSpecificationExecutor<Landlord> {
}
