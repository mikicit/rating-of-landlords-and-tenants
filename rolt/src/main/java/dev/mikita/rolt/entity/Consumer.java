package dev.mikita.rolt.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rolt_consumer")
public abstract class Consumer extends User {
    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 32)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private ConsumerGender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsumerStatus status = ConsumerStatus.ACTIVE;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        Objects.requireNonNull(firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        Objects.requireNonNull(lastName);
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        Objects.requireNonNull(phone);
        this.phone = phone;
    }

    public ConsumerGender getGender() {
        return gender;
    }

    public void setGender(ConsumerGender gender) {
        Objects.requireNonNull(gender);
        this.gender = gender;
    }

    public ConsumerStatus getStatus() {
        return status;
    }

    public void setStatus(ConsumerStatus status) {
        Objects.requireNonNull(status);
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consumer)) return false;
        if (!super.equals(o)) return false;
        Consumer consumer = (Consumer) o;
        return firstName.equals(consumer.firstName) && lastName.equals(consumer.lastName) && phone.equals(consumer.phone) && gender == consumer.gender && status == consumer.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, phone, gender, status);
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                '}';
    }
}
