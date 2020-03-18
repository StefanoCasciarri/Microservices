package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoom;
import com.bestgroup.conferenceroomservice.ConferenceRoomRepository;
import com.bestgroup.conferenceroomservice.ResourceNotFoundException;
import com.bestgroup.conferenceroomservice.responseentitystructure.RoomBookingInfo;
import com.bestgroup.conferenceroomservice.responseentitystructure.User;
import com.bestgroup.conferenceroomservice.responseentitystructure.UserBooking;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RoomBookingService {

    private RoomBookingRepository roomBookingRepository;
    private ConferenceRoomRepository conferenceRoomRepository;
    private ValidationService validationService;
    private RestTemplate restTemplate;

    @Autowired
    public RoomBookingService(RoomBookingRepository roomBookingRepository,
                              ConferenceRoomRepository conferenceRoomRepository,
                              ValidationService validationService,
                              RestTemplate restTemplate) {
        this.roomBookingRepository = roomBookingRepository;
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.validationService = validationService;
        this.restTemplate = restTemplate;
    }

    public List<RoomBookingInfo> getRoomBookingsInfo(int roomId) {

        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.findById(roomId);
        conferenceRoom.orElseThrow( () -> new ResourceNotFoundException("No such room."));

        List<RoomBooking> roomBookings = conferenceRoom.get().getRoomBookings();
        List<UserBooking>  userBookings = getUserInfo(roomBookings);
        List<RoomBookingInfo> roomBookingInfos = new ArrayList<>();
        for(RoomBooking roomBooking: roomBookings){
            RoomBookingInfo roomBookingInfo = new RoomBookingInfo();
            roomBookingInfo.setRoomBooking(roomBooking);
            roomBookingInfo.setUser(findUserbyBookingId(roomBooking.getRoomBookingId(), userBookings)); //careful: User can be null!
            roomBookingInfos.add(roomBookingInfo);
        }

        return roomBookingInfos;
    }

    private User findUserbyBookingId(int roomBookingId, List<UserBooking> userBookings) {
        for(UserBooking userBooking: userBookings){
            if (userBooking.getBookingId()== roomBookingId) return userBooking.getUserId();
        }
        return null;
    }

    private List<UserBooking> getUserInfo(List<RoomBooking> roomBookings) {

        List<Integer> bookings = new ArrayList<>();// list of bookings Ids
        roomBookings.forEach(roomBooking -> bookings.add(roomBooking.getRoomBookingId()));

        String uri = new String("http://localhost:8090/users/bookings/");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(uri)
                .queryParam("bookings", bookings);

        UserBooking[] userBookingsArray= restTemplate.getForObject(builder.toUriString(),  UserBooking[].class);
        List<UserBooking>  userBookings = Arrays.asList(userBookingsArray);

        return userBookings;

    }


    public RoomBookingInfo createRoomBooking(Integer roomId, Integer userId, RoomBooking roomBooking) {

        saveRoomBooking(roomBooking);
        saveRoomBookingtoConferenceRoom(roomId, roomBooking);
        UserBooking userBooking = saveRoomBookingtoUser(userId, roomBooking);
        return new RoomBookingInfo(roomBooking, userBooking.getUserId());

    }

    public RoomBooking saveRoomBooking(RoomBooking roomBooking) {
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

        int bookingID = roomBooking.getRoomBookingId();
        String uri = new String("http://localhost:8090/users/"+ userId +"/bookings?bookingID="+bookingID);
        ResponseEntity<UserBooking> userBookingResponseEntity;
        try {
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

    public List<RoomBooking> getBookingsInfo(List<Integer> bookings) {

        List<RoomBooking> roomBookings = roomBookingRepository.findAllById(bookings);
        return roomBookings;
    }
}
