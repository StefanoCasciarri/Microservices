package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.responseentitystructure.RoomBookingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RoomBookingController {

    private RoomBookingService roomBookingService;

    @Autowired
    public RoomBookingController(RoomBookingService roomBookingService) {
        this.roomBookingService = roomBookingService;
    }


    @PostMapping("/conference-rooms/{roomId}/bookings")
    public ResponseEntity<RoomBooking> createRoomBooking(@PathVariable Integer roomId,
                                                         @RequestParam Integer userId,
                                                         @Valid @RequestBody RoomBooking roomBooking) {
        roomBookingService.createRoomBooking(roomBooking);
        roomBookingService.saveRoomBookingtoConferenceRoom(roomId, roomBooking);
        roomBookingService.saveRoomBookingtoUser(userId, roomBooking);

        return new ResponseEntity<RoomBooking>(roomBooking, HttpStatus.CREATED);
    }

    @GetMapping("/conference-rooms/{roomId}/bookings")
    public List<RoomBookingInfo> getRoomBookings(@PathVariable int roomId) {

        return roomBookingService.getRoomBookingsInfo(roomId);
    }
}
