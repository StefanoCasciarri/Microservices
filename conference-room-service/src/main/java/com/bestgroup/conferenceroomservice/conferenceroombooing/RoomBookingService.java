package com.bestgroup.conferenceroomservice.conferenceroombooing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomBookingService {

    RoomBookingService roomBookingService;

    @Autowired
    public RoomBookingService(RoomBookingService roomBookingService) {
        this.roomBookingService = roomBookingService;
    }



}
