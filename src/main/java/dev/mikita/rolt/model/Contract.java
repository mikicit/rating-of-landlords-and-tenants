package dev.mikita.rolt.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type Contract.
 */
@Entity
@Table(name = "rolt_contract")
@NamedQuery(name = "Contract.findIntersectionsByDateRange",
        query = "SELECT c from Contract c WHERE c.property.id = :propertyId " +
                        "AND (:start <= c.endDate AND c.startDate <= :end)")
public class Contract {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "start_date", nullable = false, columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "DATE")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    /**
     * Gets created on.
     *
     * @return the created on
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets created on.
     *
     * @param createdOn the created on
     */
    public void setCreatedOn(LocalDateTime createdOn) {
        Objects.requireNonNull(createdOn);
        this.createdOn = createdOn;
    }

    /**
     * Gets start date.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(LocalDate startDate) {
        Objects.requireNonNull(startDate);
        this.startDate = startDate;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    public void setEndDate(LocalDate endDate) {
        Objects.requireNonNull(endDate);
        this.endDate = endDate;
    }

    /**
     * Gets property.
     *
     * @return the property
     */
    public Property getProperty() {
        return property;
    }

    /**
     * Sets property.
     *
     * @param property the property
     */
    public void setProperty(Property property) {
        Objects.requireNonNull(property);
        this.property = property;
    }

    /**
     * Gets tenant.
     *
     * @return the tenant
     */
    public Tenant getTenant() {
        return tenant;
    }

    /**
     * Sets tenant.
     *
     * @param tenant the tenant
     */
    public void setTenant(Tenant tenant) {
        Objects.requireNonNull(tenant);
        this.tenant = tenant;
    }

    /**
     * Pre persist.
     */
    @PrePersist
    public void prePersist() {
        if (endDate.isBefore(startDate)) {
            throw new IllegalStateException("The end date of the contract cannot be earlier than the start date of the contract.");
        }

        if (endDate.isEqual(startDate)) {
            throw new IllegalStateException("The start and end dates of a contract cannot be the same.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contract contract)) return false;
        return Objects.equals(id, contract.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", property=" + property +
                ", tenant=" + tenant +
                '}';
    }
}
