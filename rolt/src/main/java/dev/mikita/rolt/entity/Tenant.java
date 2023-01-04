package dev.mikita.rolt.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "rolt_tenant")
@DiscriminatorValue("tenant")
public class Tenant extends Consumer {
    @Column(name = "in_search")
    private Boolean inSearch = false;

    @ManyToMany
    private Set<Property> favorites;

    public Tenant() {}

    public Boolean getInSearch() {
        return inSearch;
    }

    public void setInSearch(Boolean inSearch) {
        Objects.requireNonNull(inSearch);
        this.inSearch = inSearch;
    }

    public Set<Property> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Property> favorites) {
        Objects.requireNonNull(favorites);
        this.favorites = favorites;
    }

    public void addFavorite(Property property) {
        Objects.requireNonNull(property);

        if (favorites == null) {
            favorites = new HashSet<>();
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