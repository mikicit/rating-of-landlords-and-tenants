package dev.mikita.rolt.model;

import jakarta.persistence.Table;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * The type Moderator.
 */
@Entity
@Table(name = "rolt_moderator")
@DiscriminatorValue("moderator")
public class Moderator extends User {
    /**
     * Instantiates a new Moderator.
     */
    public Moderator() {
        this.role = Role.MODERATOR;
    }
}
