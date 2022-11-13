package dev.mikita.rolt.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConsumerDetails implements Serializable {
    @Column(table="rolt_consumer_details", name = "first_name", nullable = false, length = 32)
    private String firstName;

    @Column(table="rolt_consumer_details",name = "last_name", nullable = false, length = 32)
    private String lastName;

    @Column(table="rolt_consumer_details",name = "phone", nullable = false, length = 32)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(table="rolt_consumer_details",name = "gender", nullable = false)
    private ConsumerGender gender;

    @Enumerated(EnumType.STRING)
    @Column(table="rolt_consumer_details",name = "status", nullable = false)
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
        if (!(o instanceof ConsumerDetails)) return false;
        ConsumerDetails details = (ConsumerDetails) o;
        return firstName.equals(details.firstName) && lastName.equals(details.lastName) && phone.equals(details.phone) && gender == details.gender && status == details.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phone, gender, status);
    }

    @Override
    public String toString() {
        return "ConsumerDetails{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                '}';
    }
}
