package com.bestgroup.conferenceroomservice.responseentitystructure;

import com.bestgroup.conferenceroomservice.conferenceroombooing.RoomBooking;
import lombok.Data;

@Data
public class RoomBookingInfo {

    private RoomBooking roomBooking;
    private User user; //maybe can be renamed to userInfo for clarity


}
