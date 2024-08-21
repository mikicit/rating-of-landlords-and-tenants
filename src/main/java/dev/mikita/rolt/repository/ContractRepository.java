package dev.mikita.rolt.repository;

import dev.mikita.rolt.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {
    Page<Contract> findAll(Pageable pageable, String name);

    @Query(name = "Contract.findIntersectionsByDateRange")
    List<Contract> findIntersectionsByDateRange(Long propertyId, LocalDate start, LocalDate end);
}