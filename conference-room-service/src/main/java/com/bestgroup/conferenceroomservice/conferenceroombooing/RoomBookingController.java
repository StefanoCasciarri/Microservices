package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RoomBookingController {

    private RoomBookingRepository roomBookingRepository;

    @Autowired
    public RoomBookingController(RoomBookingRepository roomBookingRepository) {
        this.roomBookingRepository = roomBookingRepository;
    }
}
