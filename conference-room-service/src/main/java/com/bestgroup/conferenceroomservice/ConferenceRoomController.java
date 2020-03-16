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

    @GetMapping("/conference-rooms/{id}")
    public ConferenceRoom getRoomById(@PathVariable Integer id){
        return roomService.getRoomById(id);
    }

    @DeleteMapping("/conference-rooms/{id}")
    public void deleteRoomById(@PathVariable Integer id){
         roomService.deleteRoomById(id);
    }

    @PutMapping("/conference-rooms/{id}")
    public ResponseEntity<ConferenceRoom> update(@PathVariable Integer id, @RequestBody ConferenceRoom room){
        room = roomService.update(id,room);
        return new ResponseEntity<ConferenceRoom>(room, HttpStatus.CREATED);
    }


}
