package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ResourceNotFoundException;
import com.bestgroup.conferenceroomservice.responseentitystructure.UserBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
        roomBooking = roomBookingService.createRoomBooking(roomId, roomBooking);

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        int bookingID = roomBooking.getRoomBookingId();
        String uri = new String("http://localhost:8090/users/"+ userId +"/bookings?bookingID="+bookingID);
        ResponseEntity<UserBooking> returns;
        try {
            returns = rt.postForEntity(uri, bookingID, UserBooking.class);
        }
        catch(Exception e){
            //TODO rollback saved info
            throw new ResourceNotFoundException("Cant save to user");
        }
        finally {
            System.out.println("FINALLY");
        }

        return new ResponseEntity<RoomBooking>(roomBooking, HttpStatus.CREATED);
    }

    @GetMapping("/conference-rooms/{id}/bookings")
    public List<RoomBooking> getRoomBookings(@PathVariable int id) {

        return roomBookingService.getRoomBookings(id);
    }
}
