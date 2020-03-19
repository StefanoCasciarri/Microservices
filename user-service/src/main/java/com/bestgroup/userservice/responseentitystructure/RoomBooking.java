package com.bestgroup.userservice.responseentitystructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomBooking {
    private int roomBookingId;
    private Date startDateTime;
    private Date endDateTime;
    private ConferenceRoom conferenceRoom;
}
