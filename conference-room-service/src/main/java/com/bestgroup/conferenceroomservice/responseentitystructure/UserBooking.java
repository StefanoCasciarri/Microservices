package com.bestgroup.conferenceroomservice.responseentitystructure;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserBooking {

    private int bookingId;
    private User userId;

}
