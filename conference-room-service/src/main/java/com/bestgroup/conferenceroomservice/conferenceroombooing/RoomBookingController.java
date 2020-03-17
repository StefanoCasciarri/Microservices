package com.bestgroup.conferenceroomservice.conferenceroombooing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomBookingController {

    private RoomBookingService roomBookingService;

    @Autowired
    public RoomBookingController(RoomBookingService roomBookingService) {
        this.roomBookingService = roomBookingService;
    }


    @PostMapping("/conference-rooms/{roomId}/bookings")
    public ResponseEntity<RoomBooking> createRoomBooking(@PathVariable Integer roomId, @RequestBody RoomBooking roomBooking) {
        roomBooking.setRoomId(roomId);
        roomBookingService.createRoomBooking(roomBooking);
        return new ResponseEntity<RoomBooking>(roomBooking, HttpStatus.CREATED);
    }

    @GetMapping("/conference-rooms/{id}/bookings")
    public List<RoomBooking> getRoomBookings(@PathVariable int id) {

        return roomBookingService.getRoomBookings(id);
    }
}
