package dev.mikita.rolt.entity;

import dev.mikita.rolt.exception.IncorrectDateRangeException;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rolt_contract")
@NamedQueries({
        @NamedQuery(name = "Contract.findByProperty", query = "SELECT c from Contract c WHERE c.property = :property"),
        @NamedQuery(name = "Contract.findByUser", query = "SELECT c from Contract c WHERE c.property.owner = :user OR c.tenant = :user"),
        @NamedQuery(name = "Contract.findIntersectionsByDateRange", query = "SELECT c from Contract c WHERE c.property = :property AND (:start <= c.endDate AND c.startDate <= :end)")
})
public class Contract {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        Objects.requireNonNull(createdOn);
        this.createdOn = createdOn;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        Objects.requireNonNull(startDate);
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate end_date) {
        Objects.requireNonNull(end_date);
        this.endDate = end_date;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        Objects.requireNonNull(property);
        this.property = property;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        Objects.requireNonNull(tenant);
        this.tenant = tenant;
    }

    @PrePersist
    public void prePersist() {
        if (endDate.isBefore(startDate)) {
            throw new IncorrectDateRangeException("The end date of the contract cannot be earlier than the start date of the contract.");
        }

        if (endDate.isEqual(startDate)) {
            throw new IncorrectDateRangeException("The start and end dates of a contract cannot be the same.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contract)) return false;
        Contract contract = (Contract) o;
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
