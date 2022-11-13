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

    @Column(name = "description")
    private String description;

    @Enumerated
    @Column(name = "status", nullable = false)
    private PublicationStatus status = PublicationStatus.PUBLISHED;

    @Column(name = "rating", nullable = false)
    private Integer rating;
}
