package dev.mikita.rolt.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@DiscriminatorValue("tenant")
@SecondaryTable(name = "rolt_consumer_details",
        pkJoinColumns = @PrimaryKeyJoinColumn(name="id"))
public class Tenant extends User {
    @Column(name = "in_search")
    private Boolean inSearch = false;

    @Embedded
    private ConsumerDetails details;

    @ManyToMany
    private List<Property> favorites;

    public Boolean getInSearch() {
        return inSearch;
    }

    public void setInSearch(Boolean inSearch) {
        Objects.requireNonNull(inSearch);
        this.inSearch = inSearch;
    }

    public ConsumerDetails getDetails() {
        return details;
    }

    public void setDetails(ConsumerDetails details) {
        Objects.requireNonNull(details);
        this.details = details;
    }

    public List<Property> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Property> favorites) {
        Objects.requireNonNull(favorites);
        this.favorites = favorites;
    }

    public void addFavorite(Property property) {
        Objects.requireNonNull(property);

        if (favorites == null) {
            favorites = new ArrayList<>();
        }

        final Optional<Property> existing = favorites.stream().filter(p -> p.getId()
                .equals(property.getId())).findAny();

        if (existing.isEmpty()) {
            favorites.add(property);
        }
    }

    public void removeFavorite(Property property) {
        Objects.requireNonNull(property);
        if (favorites == null) return;

        favorites.remove(property);
    }
}