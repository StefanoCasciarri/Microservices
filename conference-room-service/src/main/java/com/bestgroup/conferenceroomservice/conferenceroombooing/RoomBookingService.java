package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoom;
import com.bestgroup.conferenceroomservice.ConferenceRoomRepository;
import com.bestgroup.conferenceroomservice.ResourceNotFoundException;
import com.bestgroup.conferenceroomservice.responseentitystructure.UserBooking;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public RoomBooking createRoomBooking(Integer roomId, RoomBooking roomBooking) {
        validationService.isDurationValid(roomBooking);

        roomBookingRepository.save(roomBooking);

        Optional<ConferenceRoom> optionalConferenceRoom = conferenceRoomRepository.findById(roomId);
        optionalConferenceRoom.orElseThrow( () -> new ResourceNotFoundException("No such room."));

        optionalConferenceRoom.get().addRoomBookings(roomBooking);
        conferenceRoomRepository.save(optionalConferenceRoom.get());

        //TODO: ask guys if this will be called from USER Microservice or by enduser
        return roomBooking;
    }

    public List<RoomBooking> getRoomBookings(int id) {

        Optional<ConferenceRoom> optionalConferenceRoom = conferenceRoomRepository.findById(id);
        optionalConferenceRoom.orElseThrow( () -> new ResourceNotFoundException("No such room."));
        //TODO: call USER microservice to get info about user connected with booking
        //TODO: then change the retrun structure

        return optionalConferenceRoom.get().getRoomBookings();
    }

    public UserBooking saveRoomBookingtoUser(Integer userId, RoomBooking roomBooking) {
        RestTemplate restTemplate = new RestTemplate();
  ///      restTemplate.getMessageConverters().add(new StringHttpMessageConverter());//TODO is that necessary?

        int bookingID = roomBooking.getRoomBookingId();
        String uri = new String("http://localhost:8090/users/"+ userId +"/bookings?bookingID="+bookingID);
        ResponseEntity<UserBooking> userBookingResponseEntity;
        try {
            userBookingResponseEntity = restTemplate.postForEntity(uri, bookingID, UserBooking.class);
        }
        catch(Exception e){
            //TODO rollback saved info
            throw new ResourceNotFoundException("Cant save to user");
        }
        finally {
            System.out.println("FINALLY");
        }
        return  userBookingResponseEntity.getBody();
    }
}
