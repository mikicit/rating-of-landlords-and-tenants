package dev.mikita.rolt.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@DiscriminatorValue("landlord")
@SecondaryTable(name = "rolt_consumer_details",
        pkJoinColumns = @PrimaryKeyJoinColumn(name="id"))
public class Landlord extends User {
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties;

    @OneToMany(mappedBy = "reviewed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @Embedded
    private ConsumerDetails details;

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        Objects.requireNonNull(properties);
        this.properties = properties;
    }

    public void addProperty(Property property) {
        Objects.requireNonNull(property);

        if (properties == null) {
            properties = new ArrayList<>();
        }

        final Optional<Property> existing = properties.stream().filter(p -> p.getId()
                .equals(property.getId())).findAny();

        if (existing.isEmpty()) {
            properties.add(property);
            property.setOwner(this);
        }
    }

    public void removeProperty(Property property) {
        Objects.requireNonNull(property);
        if (properties == null) return;

        property.setStatus(PublicationStatus.DELETED);
    }

    public ConsumerDetails getDetails() {
        return details;
    }

    public void setDetails(ConsumerDetails details) {
        Objects.requireNonNull(details);
        this.details = details;
    }
}
