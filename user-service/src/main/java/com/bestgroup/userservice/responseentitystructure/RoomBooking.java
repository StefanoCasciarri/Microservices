package com.bestgroup.userservice.responseentitystructure;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class RoomBooking {

    private int roomBookingId;
    private Date startDateTime;
    private Date endDateTime;
    private ConferenceRoom conferenceRoom;

}
