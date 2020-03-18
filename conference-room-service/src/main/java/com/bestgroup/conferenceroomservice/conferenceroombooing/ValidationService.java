package com.bestgroup.conferenceroomservice.conferenceroombooing;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ValidationService {

    public boolean isDurationValid(RoomBooking roomBooking){
        if(roomBooking.getStartDateTime().compareTo(roomBooking.getEndDateTime()) < 0){
            return true;
        }
        else throw new BadRequestExeption("End date before start date");
    }
}
