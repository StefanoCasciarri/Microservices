package com.bestgroup.conferenceroomservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ConferenceRoomController {

    private final ConferenceRoomService roomService;

    public ConferenceRoomController(ConferenceRoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/conference-rooms")
    public ResponseEntity<ConferenceRoom> create(@Valid @RequestBody ConferenceRoom room){ roomService.createConferenceRoom(room);
        return new ResponseEntity<ConferenceRoom>(room, HttpStatus.CREATED);
    }

    @GetMapping({"/conference-rooms"})
    public ResponseEntity<List<ConferenceRoom>> getAllRooms(){

        List<ConferenceRoom> conferenceRooms = roomService.getAllRooms();
        return new ResponseEntity<>(conferenceRooms,HttpStatus.OK);
    }

    @GetMapping("/conference-rooms/{roomId}")
    public ResponseEntity<ConferenceRoom> getRoomById(@PathVariable Integer roomId){
        ConferenceRoom conferenceRoom = roomService.getRoomById(roomId);
        return new ResponseEntity<>(conferenceRoom,HttpStatus.OK);
    }

    @DeleteMapping("/conference-rooms/{roomId}")
    public void deleteRoomById(@PathVariable Integer roomId){
         roomService.deleteRoomById(roomId);
    }

    @PutMapping("/conference-rooms/{roomId}")
    public ResponseEntity<ConferenceRoom> update(@PathVariable Integer roomId, @Valid @RequestBody ConferenceRoom room){
        room = roomService.update(roomId,room);
        return new ResponseEntity<ConferenceRoom>(room, HttpStatus.CREATED);
    }


}
