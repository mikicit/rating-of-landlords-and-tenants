package dev.mikita.rolt.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("moderator")
public class Moderator extends User {
}
