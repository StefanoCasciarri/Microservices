package com.bestgroup.conferenceroomservice.responseentitystructure;


import lombok.Data;

@Data
public class UserBooking {

    private int bookingId;
    private User user;

}
