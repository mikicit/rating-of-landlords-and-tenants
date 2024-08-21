package dev.mikita.rolt.repository;

import dev.mikita.rolt.model.Consumer;
import dev.mikita.rolt.model.Contract;
import dev.mikita.rolt.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    Page<Review> findAll(Pageable pageable, String name);

    @Query(name = "Review.findAverageRatingByConsumer")
    Double findAverageRatingByConsumer(@Param("consumer") Consumer consumer);

    @Query(name = "Review.findByContractAndAuthor")
    List<Review> findByContractAndAuthor(@Param("contract") Contract contract, @Param("author") Consumer author);
}
