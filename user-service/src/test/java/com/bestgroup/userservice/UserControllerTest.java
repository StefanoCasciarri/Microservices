package com.bestgroup.userservice;


import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
import com.bestgroup.userservice.responseentitystructure.ConferenceRoom;
import com.bestgroup.userservice.responseentitystructure.RoomBooking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

     @InjectMocks
     UserController userController;
     @Mock
     UserService userService;


    @Test
    void getAllUsersReturnNonEmptyList() {

        User user1 = new User("John", "Doe");
        User user2 = new User("Anna", "Smith");
        List<User> mockUserList = new ArrayList();
        mockUserList.add(user1);
        mockUserList.add(user2);

        when(userService.retrieveAllUsers()).thenReturn(mockUserList);

        List<User> userList = userController.getAllUsers();

        assertNotNull(userList);
        assertEquals(userList.size(), 2);
        assertEquals("John", userList.get(0).getFirstName());
        assertEquals("Doe", userList.get(0).getLastName());
        assertEquals("Anna", userList.get(1).getFirstName());
        assertEquals("Smith", userList.get(1).getLastName());

    }

    @Test
    void postUser() {

        when(userService.newUser(any(User.class))).thenReturn(ResponseEntity.created(URI.create("/1")).build());

        User user = new User("John", "Doe");
        ResponseEntity<Object> responseEntity = userController.postUser(user);

        assertNotNull(responseEntity);
        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals("/1",responseEntity.getHeaders().getLocation().getPath());

    }

    @Test
    void getExistingUserByIdReturnUser() {

        when(userService.retrieveUser(anyInt())).thenReturn(new User("Thomas", "Duke"));

        User mockedUser = userController.getUser(33);

        assertNotNull(mockedUser);
        assertEquals("Thomas", mockedUser.getFirstName());
        assertEquals("Duke", mockedUser.getLastName());

    }

    @Test
    void deleteUser() {

        when(userService.removeUser(anyInt())).thenReturn(true);

        assertEquals(true, userController.deleteUser(52));

    }

    @Test
    void putUser() {

        User user = new User("John", "Doe");
        when(userService.updateUser(anyInt(), any(User.class))).thenReturn(user);

        User mockedUser = userController.putUser(5, new User("Miranda", "Hook"));

        assertNotNull(mockedUser);
        assertEquals("John", mockedUser.getFirstName());
        assertEquals("Doe", mockedUser.getLastName());

    }

    @Test
    void getUserRoomBookings() {
        List<RoomBooking> roomBookings = new ArrayList<>();

        Date startDate = new Date();
        Date endDate = new Date();
        ConferenceRoom conferenceRoom = new ConferenceRoom(2, "Test", 34);

        RoomBooking roomBookingOne = new RoomBooking(3,startDate,endDate,conferenceRoom);
        RoomBooking roomBookingTwo = new RoomBooking(4,startDate,endDate,conferenceRoom);

        roomBookings.add(roomBookingOne);
        roomBookings.add(roomBookingTwo);

        when(userService.retrieveUserBookings(anyInt())).thenReturn(roomBookings);

        List<RoomBooking> mockedRoomBookings = userController.getUserRoomBookings(32);

        assertNotNull(mockedRoomBookings);
        assertEquals(2, mockedRoomBookings.size());

        assertEquals(3, mockedRoomBookings.get(0).getRoomBookingId());
        assertEquals(4, mockedRoomBookings.get(1).getRoomBookingId());

        assertEquals(startDate, mockedRoomBookings.get(0).getStartDateTime());
        assertEquals(endDate, mockedRoomBookings.get(0).getEndDateTime());

        assertEquals(startDate, mockedRoomBookings.get(1).getStartDateTime());
        assertEquals(endDate, mockedRoomBookings.get(1).getEndDateTime());

        assertEquals(conferenceRoom, mockedRoomBookings.get(0).getConferenceRoom());
        assertEquals(conferenceRoom, mockedRoomBookings.get(1).getConferenceRoom());
    }

    @Test
    void addUserBooking() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));

        UserBooking userBooking = new UserBooking(5, new User("John", "Doe"));

        when(userService.addUserBooking(anyInt(),anyInt())).thenReturn(userBooking);

        ResponseEntity<UserBooking> responseEntity = userController.addUserBooking(5, 8);

        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    void getUserBookings() {
        List<UserBooking> userBookings = new ArrayList<>();
        userBookings.add(new UserBooking(3, new User("John", "Doe")));
        userBookings.add(new UserBooking(4, new User("Miranda", "Wall")));

        when(userService.getUserBookings(any(List.class))).thenReturn(userBookings);

        List<Integer> integerList = Arrays.asList(2,5,9);

        List<UserBooking> mockedList = userController.getUserBookings(integerList);

        assertNotNull(mockedList);
        assertEquals(2, mockedList.size());
        assertEquals(3, mockedList.get(0).getBookingId());
        assertEquals(4, mockedList.get(1).getBookingId());
    }

}