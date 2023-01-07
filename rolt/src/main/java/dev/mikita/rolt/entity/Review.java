package dev.mikita.rolt.entity;

import dev.mikita.rolt.exception.IncorrectConsumerException;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rolt_review",
        uniqueConstraints = {
                @UniqueConstraint(name="unique_author_contract", columnNames = {"author_id", "contract_id"})
})
@NamedQueries({
        @NamedQuery(name = "Review.findByAuthor", query = "SELECT r from Review r WHERE r.author = :user"),
        @NamedQuery(name = "Review.findByReviewed", query = "SELECT r from Review r WHERE r.author <> :user AND (r.contract.tenant = :user OR r.contract.property.owner = :user)")
})
public class Review implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "updated_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Consumer author;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PublicationStatus status = PublicationStatus.PUBLISHED;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        Objects.requireNonNull(contract);
        this.contract = contract;
    }

    public Consumer getAuthor() {
        return author;
    }

    public void setAuthor(Consumer author) {
        Objects.requireNonNull(author);
        this.author = author;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        Objects.requireNonNull(createdOn);
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        Objects.requireNonNull(updatedOn);

        if (updatedOn.isBefore(createdOn)) {
            throw new IllegalArgumentException("The update date must be after the creation date.");
        }

        this.updatedOn = updatedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Objects.requireNonNull(description);
        this.description = description;
    }

    public PublicationStatus getStatus() {
        return status;
    }

    public void setStatus(PublicationStatus status) {
        Objects.requireNonNull(status);
        this.status = status;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        Objects.requireNonNull(rating);

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("The rating value should be between 1 and 5.");
        }

        this.rating = rating;
    }

    @PrePersist
    public void prePersist() {
        if (!author.equals(contract.getTenant()) && !author.equals(contract.getProperty().getOwner())) {
            throw new IncorrectConsumerException("The user has no right to leave feedback for this contract");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", contract=" + contract +
                ", author=" + author +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", rating=" + rating +
                '}';
    }
}
