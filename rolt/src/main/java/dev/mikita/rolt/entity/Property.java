package dev.mikita.rolt.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "rolt_property")
public class Property implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false)
    private Date createdOn = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    private Date updatedOn;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Landlord owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PublicationStatus status = PublicationStatus.PUBLISHED;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PropertyType type;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = false;

    @Column(name = "square", nullable = false)
    private Double square;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "postal_code", nullable = false, length = 16)
    private String postalCode;

    @ManyToOne
    @JoinColumn(nullable = false)
    private City city;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
