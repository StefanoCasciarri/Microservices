package com.bestgroup.userservice.entities;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserBookings {

    @Id
    private int bookingId;
    @ManyToOne
    User userId;

    public UserBookings() {
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
}
