package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoom;
import com.bestgroup.conferenceroomservice.ConferenceRoomRepository;
import com.bestgroup.conferenceroomservice.ResourceNotFoundException;
import com.bestgroup.conferenceroomservice.responseentitystructure.RoomBookingInfo;
import com.bestgroup.conferenceroomservice.responseentitystructure.User;
import com.bestgroup.conferenceroomservice.responseentitystructure.UserBooking;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
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


    public RoomBookingInfo createRoomBooking(Integer roomId, Integer userId, RoomBooking roomBooking) {

        validateRoomBookingParameters(roomId, roomBooking);
        saveRoomBooking(roomBooking);
        saveRoomBookingtoConferenceRoom(roomId, roomBooking);
        System.out.println(conferenceRoomRepository.findAll());
        System.out.println(roomBookingRepository.findAll());
        UserBooking userBooking = saveRoomBookingtoUser(userId, roomBooking);
        return new RoomBookingInfo(roomBooking, userBooking.getUserId());
    }


    public boolean validateRoomBookingParameters(Integer roomId, RoomBooking roomBooking){
        validationService.isRoomExist(roomId);
        validationService.isDurationValid(roomBooking);
        validationService.isRoomAvailable(roomId, roomBooking);
        return true;
    }


    public RoomBooking saveRoomBooking(RoomBooking roomBooking) {

        return roomBookingRepository.save(roomBooking);
    }

    public ConferenceRoom saveRoomBookingtoConferenceRoom(Integer roomId, RoomBooking roomBooking) {

        validationService.isRoomExist(roomId);// make sure room exists, when saving a room to repo made at beginning so no booking was saved when no room
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.findById(roomId);
        conferenceRoom.get().getRoomBookings().add(roomBooking);//add room booking to collection in conference room
        roomBooking.setConferenceRoom(conferenceRoom.get());
        conferenceRoomRepository.save(conferenceRoom.get());

        return conferenceRoom.get();

    }

    public UserBooking saveRoomBookingtoUser(Integer userId, RoomBooking roomBooking) {

        HttpEntity entity = this.createTokenHeader();

        int bookingID = roomBooking.getRoomBookingId();
        String uri = new String("http://localhost:8090/users/"+ userId +"/bookings?bookingID="+bookingID);

        try {
            ResponseEntity<UserBooking> userBookingResponseEntity = userBookingResponseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, UserBooking.class);
            return userBookingResponseEntity.getBody();

        }
        catch(Exception e){
            //if 404 : cant save to user then delete room booking
            deleteRoomBooking(roomBooking);
            throw new ResourceNotFoundException("Cant save to user");
        }
    }

    public void deleteRoomBooking(RoomBooking roomBooking) {
        //first delete roomBooking form conferenceRoom collection
        deleteRoomBookingfromConferenceRoom(roomBooking);
        roomBookingRepository.delete(roomBooking);
    }

    public void deleteRoomBookingfromConferenceRoom(RoomBooking roomBooking) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.findById(roomBooking.getConferenceRoom().getRoomId());
        conferenceRoom.orElseThrow( () -> new ResourceNotFoundException("No such room."));
        conferenceRoom.get().getRoomBookings().remove(roomBooking);
    }


    //get info about bookings for a room by room id
    public List<RoomBookingInfo> getRoomBookingsInfo(int roomId) {

        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.findById(roomId);
        conferenceRoom.orElseThrow( () -> new ResourceNotFoundException("No such room."));

        List<RoomBooking> roomBookings = conferenceRoom.get().getRoomBookings();
        List<UserBooking>  userBookings = getUserInfo(roomBookings);
        List<RoomBookingInfo> roomBookingInfos =  retrieveRoomBookingsInfo(roomBookings, userBookings);

        return roomBookingInfos;
    }

    // call User Microservice to get Info about Users who have those Bookings
    public List<UserBooking> getUserInfo(List<RoomBooking> roomBookings) {

        List<Integer> bookingsIds = retrieveRoomBookingsIds(roomBookings);
        String uri = createUriCall(bookingsIds);

        UserBooking[] userBookingsArray= restTemplate.getForObject(uri,  UserBooking[].class);
        List<UserBooking>  userBookings = Arrays.asList(userBookingsArray);

        return userBookings;
    }

    public List<Integer> retrieveRoomBookingsIds(List<RoomBooking> roomBookings) {

        List<Integer> bookingsIds = new ArrayList<>();// list of bookings Ids
        roomBookings.forEach(roomBooking -> bookingsIds.add(roomBooking.getRoomBookingId()));
        return bookingsIds;
    }

    private String createUriCall(List<Integer> bookingsIds) {

        String uri = new String("http://localhost:8090/users/bookings/");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(uri)
                .queryParam("bookings", bookingsIds);
        return  builder.toUriString();
    }

    //retrieve info from room bookings and user bookings and connect them into room bookings info
    public List<RoomBookingInfo> retrieveRoomBookingsInfo(List<RoomBooking> roomBookings, List<UserBooking> userBookings) {
        List<RoomBookingInfo> roomBookingInfos = new ArrayList<>();
        for(RoomBooking roomBooking: roomBookings){
            RoomBookingInfo roomBookingInfo = new RoomBookingInfo();
            roomBookingInfo.setRoomBooking(roomBooking);
            roomBookingInfo.setUserInfo(findUserbyBookingId(roomBooking.getRoomBookingId(), userBookings)); //careful: User can be null!
            roomBookingInfos.add(roomBookingInfo);
        }
        return roomBookingInfos;
    }

    //Connect booking id with user id
    public User findUserbyBookingId(int roomBookingId, List<UserBooking> userBookings) {
        for(UserBooking userBooking: userBookings){
            if (userBooking.getBookingId()== roomBookingId) return userBooking.getUserId();
        }
        return null;
    }

    //get info about bookings by their booings ids
    public List<RoomBooking> getBookingsInfo(List<Integer> bookings) {

        List<RoomBooking> roomBookings = roomBookingRepository.findAllById(bookings);
        return roomBookings;
    }

    private String getTokenFromRequest(){
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder
                        .currentRequestAttributes())
                .getRequest();
        String value = request.getHeader("Authorization").split(" ")[1];
        return value;
    }

    private HttpEntity createTokenHeader(){
        String token = this.getTokenFromRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity(headers);
    }
}
