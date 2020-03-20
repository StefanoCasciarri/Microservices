package com.bestgroup.conferenceroomservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConferenceRoomService {

    private final ConferenceRoomRepository conferenceRoomRepository;

    public ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository) {
        this.conferenceRoomRepository = conferenceRoomRepository;
    }

    public ConferenceRoom createConferenceRoom(ConferenceRoom room) {
       return conferenceRoomRepository.save(room);
    }

    public List<ConferenceRoom> getAllRooms() {
        return conferenceRoomRepository.findAll();
    }

    public ConferenceRoom getRoomById(Integer roomId) {
        return conferenceRoomRepository.findById(roomId).orElseThrow( () -> new ResourceNotFoundException("No such room."));
    }

    public void deleteRoomById(Integer roomId) {
        ConferenceRoom conferenceRoom =
                conferenceRoomRepository.findById(roomId).orElseThrow( () -> new ResourceNotFoundException("No such room."));
        if(conferenceRoom != null)
            conferenceRoomRepository.deleteById(roomId);
    }

    public ConferenceRoom update(Integer roomId, ConferenceRoom room) {

        ConferenceRoom roomEntityUpdated = conferenceRoomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("No such room."));

        roomEntityUpdated.setFloor(room.getFloor());
        roomEntityUpdated.setName(room.getName());
        roomEntityUpdated.setSize(room.getSize());

        return conferenceRoomRepository.save(roomEntityUpdated);

    }
}
