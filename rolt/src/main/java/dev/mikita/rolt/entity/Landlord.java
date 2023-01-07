package dev.mikita.rolt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "rolt_landlord")
@DiscriminatorValue("landlord")
public class Landlord extends Consumer {
    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Property> properties;

    public Landlord() {
        this.role = Role.LANDLORD;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        Objects.requireNonNull(properties);
        this.properties = properties;
    }

    public void addProperty(Property property) {
        Objects.requireNonNull(property);

        if (properties == null) {
            properties = new HashSet<>();
        }

        properties.add(property);
        property.setOwner(this);
    }

    public void removeProperty(Property property) {
        Objects.requireNonNull(property);
        if (properties == null) return;

        properties.remove(property);
    }
}
