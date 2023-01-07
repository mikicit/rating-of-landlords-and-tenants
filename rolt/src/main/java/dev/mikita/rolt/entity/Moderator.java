package dev.mikita.rolt.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rolt_moderator")
@DiscriminatorValue("moderator")
public class Moderator extends User {
    public Moderator() {
        this.role = Role.MODERATOR;
    }
}
