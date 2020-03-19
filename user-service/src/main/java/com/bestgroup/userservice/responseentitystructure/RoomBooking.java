package com.bestgroup.userservice.responseentitystructure;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class RoomBooking {
    private int roomBookingId;
    private Date startDateTime;
    private Date endDateTime;
    private ConferenceRoom conferenceRoom;
}
