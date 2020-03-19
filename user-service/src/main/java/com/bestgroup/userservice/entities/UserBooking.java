package com.bestgroup.userservice.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
public class UserBooking {

    @Id
    @Positive
    private int bookingId;
    @NotNull
    @ManyToOne
    private User userId;


    public UserBooking() {}

    public UserBooking(int bookingId, User userId) {
      this.userId = userId;
      this.bookingId = bookingId;
    }
    public int getBookingId() {
        return bookingId;
    }

    public User getUserId() {
        return userId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserBookings{" +
                "bookingId=" + bookingId +
                ", userId=" + userId.getId() +
                '}';
    }
}
