package dev.mikita.rolt.model;

import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type User.
 */
@Entity
@Table(name = "rolt_user")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@NamedQuery(name = "User.findByEmail", query = "SELECT u from User u WHERE u.email = :email")
public abstract class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "email", nullable = false, length = 64, unique = true)
    protected String email;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP")
    protected LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "last_login", columnDefinition = "TIMESTAMP")
    protected LocalDateTime lastLogin;

    /**
     * The Role.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    protected Role role;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        Objects.requireNonNull(email);
        this.email = email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        Objects.requireNonNull(password);
        this.password = password;
    }

    /**
     * Encode password.
     *
     * @param encoder the encoder
     */
    public void encodePassword(PasswordEncoder encoder) {
        Objects.requireNonNull(encoder);
        this.password = encoder.encode(password);
    }

    /**
     * Erase password.
     */
    public void erasePassword() {
        this.password = null;
    }

    /**
     * Gets created on.
     *
     * @return the created on
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets created on.
     *
     * @param createdOn the created on
     */
    public void setCreatedOn(LocalDateTime createdOn) {
        Objects.requireNonNull(createdOn);
        this.createdOn = createdOn;
    }

    /**
     * Gets last login.
     *
     * @return the last login
     */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets last login.
     *
     * @param lastLogin the last login
     */
    public void setLastLogin(LocalDateTime lastLogin) {
        Objects.requireNonNull(lastLogin);

        if (lastLogin.isBefore(createdOn)) {
            throw new IllegalStateException("The date of the last login must be later than the creation date.");
        }

        this.lastLogin = lastLogin;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(Role role) {
        Objects.requireNonNull(role);
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", createdOn=" + createdOn +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
