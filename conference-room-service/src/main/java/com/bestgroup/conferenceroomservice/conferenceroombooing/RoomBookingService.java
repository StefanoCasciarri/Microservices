package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoom;
import com.bestgroup.conferenceroomservice.ConferenceRoomRepository;
import com.bestgroup.conferenceroomservice.ResourceNotFoundException;
import com.bestgroup.conferenceroomservice.responseentitystructure.UserBooking;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomBookingService {

    private RoomBookingRepository roomBookingRepository;
    private ConferenceRoomRepository conferenceRoomRepository;
    private ValidationService validationService;

    @Autowired
    public RoomBookingService(RoomBookingRepository roomBookingRepository,
                              ConferenceRoomRepository conferenceRoomRepository,
                              ValidationService validationService) {
        this.roomBookingRepository = roomBookingRepository;
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.validationService = validationService;
    }

    public List<RoomBooking> getRoomBookings(int id) {

        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.findById(id);
        conferenceRoom.orElseThrow( () -> new ResourceNotFoundException("No such room."));

        List<RoomBooking> roomBookings = conferenceRoom.get().getRoomBookings();
        //getUserInfo(roomBookings);TODO finish this. parameter bookings does not work

        //TODO: call USER microservice to get info about user connected with booking
        //TODO: then change the retrun structure

        return conferenceRoom.get().getRoomBookings();
    }

    private void getUserInfo(List<RoomBooking> roomBookings) {
        //TODO finish this. parameter bookings does not work
        List<Integer> bookings = new ArrayList<>();// list of bookings Ids
        roomBookings.forEach(roomBooking -> bookings.add(roomBooking.getRoomBookingId()));

        RestTemplate restTemplate = new RestTemplate();
        String uri = new String("http://localhost:8090/users/bookings/");
        ResponseEntity<UserBooking[]> userBookings= restTemplate.getForEntity(uri,  UserBooking[].class, bookings);


    }


    public RoomBooking createRoomBooking(RoomBooking roomBooking) {
        validationService.isDurationValid(roomBooking);
        roomBookingRepository.save(roomBooking);

        return roomBooking;
    }

    public ConferenceRoom saveRoomBookingtoConferenceRoom(Integer roomId, RoomBooking roomBooking) {
        Optional<ConferenceRoom> optionalConferenceRoom = conferenceRoomRepository.findById(roomId);
        ConferenceRoom conferenceRoom;
        if(optionalConferenceRoom.isPresent()){
            conferenceRoom = optionalConferenceRoom.get();
            conferenceRoom.getRoomBookings().add(roomBooking);
            roomBooking.setConferenceRoom(conferenceRoom);
            conferenceRoomRepository.save(conferenceRoom);
        }
        else{
            //if room doesnt exist then delete saved roomBooking
            roomBookingRepository.delete(roomBooking);
            throw new ResourceNotFoundException("No such room.");
        }
        return conferenceRoom;
    }

    public UserBooking saveRoomBookingtoUser(Integer userId, RoomBooking roomBooking) {
        RestTemplate restTemplate = new RestTemplate();

        int bookingID = roomBooking.getRoomBookingId();
        String uri = new String("http://localhost:8090/users/"+ userId +"/bookings?bookingID="+bookingID);
        ResponseEntity<UserBooking> userBookingResponseEntity;
        try {
            //TODO maybe?: post For Entity doesnt work without Object parameter, but call doesnt see bookingID Request parameter so now its put into uri.
            userBookingResponseEntity = restTemplate.postForEntity(uri, bookingID, UserBooking.class);
        }
        catch(Exception e){
            //if 404 : cant save to user then delete room booking
            deleteRoomBooking(roomBooking);
            throw new ResourceNotFoundException("Cant save to user");
        }
        return  userBookingResponseEntity.getBody();
    }

    private void deleteRoomBooking(RoomBooking roomBooking) {
        //first delete roomBooking form conferenceRoom collection
        deleteRoomBookingfromConferenceRoom(roomBooking);
        roomBookingRepository.delete(roomBooking);
    }

    private void deleteRoomBookingfromConferenceRoom(RoomBooking roomBooking) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.findById(roomBooking.getConferenceRoom().getRoomId());
        conferenceRoom.orElseThrow( () -> new ResourceNotFoundException("No such room."));
        conferenceRoom.get().getRoomBookings().remove(roomBooking);
    }
}
