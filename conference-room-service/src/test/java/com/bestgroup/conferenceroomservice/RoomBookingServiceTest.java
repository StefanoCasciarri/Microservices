package com.bestgroup.conferenceroomservice;


import com.bestgroup.conferenceroomservice.conferenceroombooing.RoomBooking;
import com.bestgroup.conferenceroomservice.conferenceroombooing.RoomBookingRepository;
import com.bestgroup.conferenceroomservice.conferenceroombooing.RoomBookingService;
import com.bestgroup.conferenceroomservice.conferenceroombooing.ValidationService;
import com.bestgroup.conferenceroomservice.responseentitystructure.User;
import com.bestgroup.conferenceroomservice.responseentitystructure.UserBooking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RoomBookingServiceTest {

    @InjectMocks
    RoomBookingService roomBookingService;

    @Mock
    RoomBookingRepository roomBookingRepository;

    @Mock
    ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    ValidationService validationService;

    @Mock
    RestTemplate restTemplate;

    @Test
    public void getBookingsInfoReturnRoomBookingList(){

        ConferenceRoom conferenceRoom = new ConferenceRoom(12, "TEST1234", 13);

        RoomBooking roomBooking1 = new RoomBooking();
        roomBooking1.setConferenceRoom(conferenceRoom);
        roomBooking1.setStartDateTime(new Date(1234L));
        roomBooking1.setEndDateTime(new Date(1235L));

        RoomBooking roomBooking2 = new RoomBooking();
        roomBooking2.setConferenceRoom(conferenceRoom);
        roomBooking2.setStartDateTime(new Date(1236L));
        roomBooking2.setEndDateTime(new Date(1237L));

        List<RoomBooking> roomBookingList = new ArrayList();
        roomBookingList.add(roomBooking1);
        roomBookingList.add(roomBooking2);

        when(roomBookingRepository.findAllById(any())).thenReturn(roomBookingList);

        assertThat(roomBookingService.getBookingsInfo(Arrays.asList(1,2,3)).size()).isEqualTo(2);

    }

    @Test
    public void getBookingsInfoReturnEmptyRoomBookingList(){

        List<RoomBooking> roomBookingList = new ArrayList();

        when(roomBookingRepository.findAllById(any())).thenReturn(roomBookingList);

        assertThat(roomBookingService.getBookingsInfo(Arrays.asList(1,2,3)).size()).isEqualTo(0);

    }

    @Test
    public void findUserByBookingIdReturnUser(){

        User user1 = new User();
        user1.setUserId(1);
        user1.setFirstName("Name");

        UserBooking userBooking1 = new UserBooking();
        userBooking1.setUserId(user1);
        userBooking1.setBookingId(1);

        UserBooking userBooking2 = new UserBooking();
        userBooking2.setUserId(user1);
        userBooking2.setBookingId(2);

        List<UserBooking> userBookingList = new LinkedList<>();
        userBookingList.add(userBooking1);
        userBookingList.add(userBooking2);

        User userFounded = roomBookingService.findUserbyBookingId(1,userBookingList);

        assertThat(userFounded.getUserId()).isEqualTo(1);

    }

    @Test
    public void findUserByBookingReturnNull(){

        User user1 = new User();
        user1.setUserId(1);
        user1.setFirstName("Name");

        UserBooking userBooking1 = new UserBooking();
        userBooking1.setUserId(user1);
        userBooking1.setBookingId(1);

        UserBooking userBooking2 = new UserBooking();
        userBooking2.setUserId(user1);
        userBooking2.setBookingId(2);

        List<UserBooking> userBookingList = new LinkedList<>();
        userBookingList.add(userBooking1);
        userBookingList.add(userBooking2);

        User userFounded = roomBookingService.findUserbyBookingId(3,userBookingList);

        assertThat(userFounded).isEqualTo(null);
    }

    @Test
    public void saveRoomBookingReturnRoomBooking(){
        RoomBooking roomBooking1 = new RoomBooking();
        ConferenceRoom conferenceRoom = new ConferenceRoom(12, "TEST1234", 13);
        roomBooking1.setConferenceRoom(conferenceRoom);
        roomBooking1.setStartDateTime(new Date(1234L));
        roomBooking1.setEndDateTime(new Date(1235L));

        when(roomBookingRepository.save(any())).thenReturn(roomBooking1);

        RoomBooking roomBookingReturned = roomBookingService.saveRoomBooking(roomBooking1);

        assertThat(roomBookingReturned.getStartDateTime()).isEqualTo(roomBooking1.getStartDateTime());
        assertThat(roomBookingReturned.getConferenceRoom()).isEqualTo(roomBooking1.getConferenceRoom());

    }

    //UNFINISHED
//    @Test
//    public void getRoomBookingsInfoReturnRoomBookingInfo(){
//        int roomId = 4;
//        ConferenceRoom conferenceRoom = new ConferenceRoom(12, "TEST1234", 13);
//        RoomBooking roomBooking1 = new RoomBooking();
//        roomBooking1.setConferenceRoom(conferenceRoom);
//        roomBooking1.setStartDateTime(new Date(1234L));
//        roomBooking1.setEndDateTime(new Date(1235L));
//        conferenceRoom.setRoomBookings(Arrays.asList(roomBooking1));
//
//        when(roomBookingService.retrieveRoomBookingsIds(any(List.class)))
//                .thenReturn(Arrays.asList(1));
//
//        List<UserBooking>  userBookings = new ArrayList<>();
//        when(roomBookingService.getUserInfo(any()))
//                .thenReturn(userBookings);
//
//        RoomBookingInfo roomBookingInfo = new RoomBookingInfo();
//        roomBookingInfo.setUser(new User(1, "Adam", "Nowak"));
//        roomBookingInfo.setRoomBooking(roomBooking1);
//
//        when(roomBookingService.retrieveRoomBookingsInfo(any(), any()))
//                .thenReturn(Arrays.asList(roomBookingInfo));
//
//        assertThat(roomBookingService.getRoomBookingsInfo(roomId))
//                .isEqualTo(Arrays.asList(roomBookingInfo));
//
//    }

    @Test
    public void getRoomBookingsInfoResourceNotFoundException(){
        int roomId = 4;

        Optional<ConferenceRoom> conferenceRoom = Optional.ofNullable(null);
        when(conferenceRoomRepository.findById(any())).thenReturn(conferenceRoom);

        assertThrows(ResourceNotFoundException.class, () ->roomBookingService.getRoomBookingsInfo(roomId));

    }


}
