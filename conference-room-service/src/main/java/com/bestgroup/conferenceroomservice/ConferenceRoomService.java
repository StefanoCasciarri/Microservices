package com.bestgroup.conferenceroomservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConferenceRoomService {

    private final ConferenceRoomRepository conferenceRoomRepository;

    public ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository) {
        this.conferenceRoomRepository = conferenceRoomRepository;
    }

    public void createConferenceRoom(ConferenceRoom room) {
        conferenceRoomRepository.save(room);
    }

    public List<ConferenceRoom> getAllRooms() {
        return conferenceRoomRepository.findAll();
    }

    public ConferenceRoom getRoomById(Integer id) {
        return conferenceRoomRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("No such room."));
    }

    public void deleteRoomById(Integer id) {
        conferenceRoomRepository.deleteById(id);
    }

    public ConferenceRoom update(Integer id, ConferenceRoom room) {

        ConferenceRoom roomEntityUpdated = conferenceRoomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No such room."));


        roomEntityUpdated.setFloor(room.getFloor());
        roomEntityUpdated.setName(room.getName());
        roomEntityUpdated.setSize(room.getSize());

        return conferenceRoomRepository.save(roomEntityUpdated);

    }
}
