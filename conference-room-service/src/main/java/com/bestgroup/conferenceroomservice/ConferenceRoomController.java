package com.bestgroup.conferenceroomservice;

import org.springframework.beans.factory.annotation.Autowired;

public class ConferenceRoomController {

    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    public ConferenceRoomController(ConferenceRoomRepository conferenceRoomRepository) {
        this.conferenceRoomRepository = conferenceRoomRepository;
    }


}
