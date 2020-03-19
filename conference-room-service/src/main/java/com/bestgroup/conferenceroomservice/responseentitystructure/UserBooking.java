package com.bestgroup.conferenceroomservice.responseentitystructure;


import lombok.Data;

@Data
public class UserBooking {

    private int bookingId;
    private User userId; //TODO change name to user both here and in UserMS

}
