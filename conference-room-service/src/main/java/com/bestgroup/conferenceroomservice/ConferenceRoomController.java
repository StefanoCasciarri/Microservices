package com.bestgroup.conferenceroomservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConferenceRoomController {

    private final ConferenceRoomService roomService;

    public ConferenceRoomController(ConferenceRoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/conference-rooms")
    public ResponseEntity<ConferenceRoom> create(@RequestBody ConferenceRoom room){ roomService.createConferenceRoom(room);
        return new ResponseEntity<ConferenceRoom>(room, HttpStatus.CREATED);
    }

    @GetMapping({"/conference-rooms"})
    public List<ConferenceRoom> getAllUser(){
        return roomService.getAllRooms();
    }

    @GetMapping("/conference-rooms/{roomId}")
    public ConferenceRoom getRoomById(@PathVariable Integer roomId){
        return roomService.getRoomById(roomId);
    }

    @DeleteMapping("/conference-rooms/{roomId}")
    public void deleteRoomById(@PathVariable Integer roomId){
         roomService.deleteRoomById(roomId);
    }

    @PutMapping("/conference-rooms/{roomId}")
    public ResponseEntity<ConferenceRoom> update(@PathVariable Integer roomId, @RequestBody ConferenceRoom room){
        room = roomService.update(roomId,room);
        return new ResponseEntity<ConferenceRoom>(room, HttpStatus.CREATED);
    }


}
