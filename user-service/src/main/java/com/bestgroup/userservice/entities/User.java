package com.bestgroup.userservice.entities;


import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="tuser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String firstName;
    private String lastName;

    @OneToMany( mappedBy = "userId")
    private List<UserBookings> bookings;


    public User() {}

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
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
