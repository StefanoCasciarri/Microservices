package com.bestgroup.userservice.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name="tuser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @NotNull
    @Size(min=2, max=30)
    private String firstName;

    @NotNull
    @Size(min=2, max=30)
    private String lastName;


    @JsonBackReference
    @OneToMany( fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userId",
              orphanRemoval = true)
    private List<UserBookings> bookings;


    public User() {}

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", bookings=" + bookings +
                '}';
    }

    public int getId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<UserBookings> getBookings() {
        return bookings;
    }

    public void setBookings(List<UserBookings> bookings) {
        this.bookings = bookings;
    }
}
