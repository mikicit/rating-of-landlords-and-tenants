package dev.mikita.rolt.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "rolt_review",
        uniqueConstraints = {
                @UniqueConstraint(name="unique_author_reviewed_contract", columnNames = {"author_id", "reviewed_id", "contract_id"})
})
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "reviewed_id", nullable = false)
    private User reviewed;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false)
    private Date createdOn = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        Objects.requireNonNull(author);
        this.author = author;
    }

    public User getReviewed() {
        return reviewed;
    }

    public void setReviewed(User reviewed) {
        Objects.requireNonNull(reviewed);
        this.reviewed = reviewed;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        Objects.requireNonNull(contract);
        this.contract = contract;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        Objects.requireNonNull(createdOn);
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        Objects.requireNonNull(updatedOn);

        if (updatedOn.before(createdOn)) {
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
}
