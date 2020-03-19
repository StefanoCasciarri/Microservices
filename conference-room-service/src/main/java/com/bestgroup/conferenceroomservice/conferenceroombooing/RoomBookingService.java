package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoom;
import com.bestgroup.conferenceroomservice.ConferenceRoomRepository;
import com.bestgroup.conferenceroomservice.ResourceNotFoundException;
import com.bestgroup.conferenceroomservice.responseentitystructure.RoomBookingInfo;
import com.bestgroup.conferenceroomservice.responseentitystructure.User;
import com.bestgroup.conferenceroomservice.responseentitystructure.UserBooking;
import com.bestgroup.conferenceroomservice.security.TokenString;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
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
                              RestTemplate restTemplate,
                              TokenString tokenString) {
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
        HttpEntity entity = this.createTokenHeader();
        ResponseEntity<UserBooking[]> userBookingsArray= restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,  UserBooking[].class);
        List<UserBooking>  userBookings = Arrays.asList(userBookingsArray.getBody());

        return userBookings;

    }


    public RoomBookingInfo createRoomBooking(Integer roomId, Integer userId, RoomBooking roomBooking) {
        validateRoomBookingParameters(roomId, roomBooking);
        saveRoomBooking(roomBooking);
        saveRoomBookingtoConferenceRoom(roomId, roomBooking);
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
        roomBookingRepository.save(roomBooking);

        return roomBooking;
    }

    public ConferenceRoom saveRoomBookingtoConferenceRoom(Integer roomId, RoomBooking roomBooking) {
        Optional<ConferenceRoom> optionalConferenceRoom = conferenceRoomRepository.findById(roomId);
        ConferenceRoom conferenceRoom;
        if(optionalConferenceRoom.isPresent()){//check not nesecary when used validationService.isRoomExist(roomId);
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

        HttpEntity entity = this.createTokenHeader();

        int bookingID = roomBooking.getRoomBookingId();
        String uri = new String("http://localhost:8090/users/"+ userId +"/bookings?bookingID="+bookingID);
        ResponseEntity<UserBooking> userBookingResponseEntity;
        try {
            userBookingResponseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, UserBooking.class);
        }
        catch(Exception e){
            //if 404 : cant save to user then delete room booking
            deleteRoomBooking(roomBooking);
            throw new ResourceNotFoundException("Cant save to user: "+ e.getMessage());
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
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity(headers);
    }
}
