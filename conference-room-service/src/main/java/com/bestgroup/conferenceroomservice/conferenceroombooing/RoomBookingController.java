package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoom;
import com.bestgroup.conferenceroomservice.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RoomBookingController {

    private RoomBookingRepository roomBookingRepository;
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    public RoomBookingController(RoomBookingRepository roomBookingRepository,ConferenceRoomRepository conferenceRoomRepository) {
        this.roomBookingRepository = roomBookingRepository;
        this.conferenceRoomRepository = conferenceRoomRepository;
    }


    @PostMapping("/conference-rooms/{id}/bookings")
    public void createRoomBooking(@RequestBody RoomBooking roomBooking) {
        RoomBooking savedRoomBooking= roomBookingRepository.save(roomBooking);
        Optional<ConferenceRoom> optionalConferenceRoom = conferenceRoomRepository.findById(roomBooking.getUserId());
        optionalConferenceRoom.get().addRoomBookings(roomBooking);
        conferenceRoomRepository.save(optionalConferenceRoom.get());
        //TODO: ask guys if this will be called from USER Microservice or by enduser

    }

    @GetMapping("/conference-rooms/{id}/bookings")
    public List<RoomBooking> retrieveRoomBookings(@PathVariable int id) {
        Optional<ConferenceRoom> optionalConferenceRoom = conferenceRoomRepository.findById(id);
        //TODO: call USER microservice to get info about user connected with booking
        //TODO: then change the retrun structure
        return optionalConferenceRoom.get().getRoomBookings();
    }
}
