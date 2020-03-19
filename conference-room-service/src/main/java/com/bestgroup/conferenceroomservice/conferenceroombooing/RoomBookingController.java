package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.responseentitystructure.RoomBookingInfo;
import com.bestgroup.conferenceroomservice.responseentitystructure.UserBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
public class RoomBookingController {

    private RoomBookingService roomBookingService;

    @Autowired
    public RoomBookingController(RoomBookingService roomBookingService) {
        this.roomBookingService = roomBookingService;
    }


    @PostMapping("/conference-rooms/{roomId}/bookings")
    public ResponseEntity<RoomBookingInfo> createRoomBooking(@PathVariable Integer roomId,
                                                         @RequestParam Integer userId,
                                                         @Valid @RequestBody RoomBooking roomBooking) {

        RoomBookingInfo roomBookingInfo = roomBookingService.createRoomBooking(roomId, userId, roomBooking);
        return new ResponseEntity<RoomBookingInfo>(roomBookingInfo, HttpStatus.CREATED);
    }

    @GetMapping("/conference-rooms/{roomId}/bookings")
    public List<RoomBookingInfo> getRoomBookings(@PathVariable int roomId) {

        return roomBookingService.getRoomBookingsInfo(roomId);
    }

    @GetMapping("conference-rooms/bookings/")
    public List<RoomBooking> getBookings(@RequestParam List<Integer> bookings){

        List<RoomBooking> roomBookings = roomBookingService.getBookingsInfo(bookings);
        return roomBookings;
    }
}
