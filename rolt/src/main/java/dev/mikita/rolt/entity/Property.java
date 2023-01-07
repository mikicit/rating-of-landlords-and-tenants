package dev.mikita.rolt.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "rolt_property")
@NamedQueries({
        @NamedQuery(name = "Property.findByOwner", query = "SELECT p from Property p WHERE p.owner = :owner"),
        @NamedQuery(name = "Property.findByOwnerPublished", query = "SELECT p from Property p WHERE p.owner = :owner AND p.status = dev.mikita.rolt.entity.PublicationStatus.PUBLISHED")
})
public class Property implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "updated_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Landlord owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PublicationStatus status = PublicationStatus.PUBLISHED;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PropertyType type;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "square", nullable = false)
    private Double square;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "postal_code", nullable = false, length = 16)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "city", nullable = false)
    private City city;

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

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        Objects.requireNonNull(updatedOn);

        if (updatedOn.isBefore(createdOn)) {
            throw new IllegalArgumentException("The date of the last login must be later than the creation date.");
        }

        this.updatedOn = updatedOn;
    }

    public Landlord getOwner() {
        return owner;
    }

    public void setOwner(Landlord owner) {
        Objects.requireNonNull(owner);
        this.owner = owner;
    }

    public PublicationStatus getStatus() {
        return status;
    }

    public void setStatus(PublicationStatus status) {
        Objects.requireNonNull(status);
        this.status = status;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        Objects.requireNonNull(type);
        this.type = type;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        Objects.requireNonNull(available);
        isAvailable = available;
    }

    public Double getSquare() {
        return square;
    }

    public void setSquare(Double square) {
        Objects.requireNonNull(square);
        this.square = square;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Objects.requireNonNull(description);
        this.description = description;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        Objects.requireNonNull(street);
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        Objects.requireNonNull(postalCode);
        this.postalCode = postalCode;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        Objects.requireNonNull(city);
        this.city = city;
    }

    @PreUpdate
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;
        Property property = (Property) o;
        return Objects.equals(id, property.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", owner=" + owner +
                ", status=" + status +
                ", type=" + type +
                ", isAvailable=" + isAvailable +
                ", square=" + square +
                ", description='" + description + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city=" + city +
                '}';
    }
}
