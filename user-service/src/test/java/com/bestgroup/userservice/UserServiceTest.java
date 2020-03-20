package com.bestgroup.userservice;


import com.bestgroup.userservice.Exceptions.UserNotFoundException;
import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBookingRepository userBookingRepository;

    @Test
    void retrieveAllUsers() {
        List<User> userList = new ArrayList<>();

        userList.add(new User("John", "Smith"));
        userList.add(new User("Terry", "Bogard"));

        when(userRepository.findAll()).thenReturn(userList);

        List<User> fetchedList = userService.retrieveAllUsers();

        assertNotNull(fetchedList);
        assertEquals(2, fetchedList.size());
        assertEquals("John", fetchedList.get(0).getFirstName());
        assertEquals("Smith", fetchedList.get(0).getLastName());
        assertEquals("Terry", fetchedList.get(1).getFirstName());
        assertEquals("Bogard", fetchedList.get(1).getLastName());
    }

    @Test
    void retrieveUser() {
        Optional<User> userOptional = Optional.of(new User("John", "Doe"));
        when(userRepository.findById(anyInt())).thenReturn(userOptional);

        User mockedUser = userService.retrieveUser(33);

        assertNotNull(mockedUser);
        assertEquals("John", mockedUser.getFirstName());
        assertEquals("Doe", mockedUser.getLastName());
    }


    @Test
    void retrieveUserThrowUserNotFoundException() {
        Optional<User> userOptional = Optional.ofNullable(null);
        when(userRepository.findById(anyInt())).thenReturn(userOptional);

        assertThrows(UserNotFoundException.class,
                () -> userService.retrieveUser(33));
    }

    @Test
    void newUser() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));
        User user = new User("John", "Doe");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<Object> responseEntity = userService.newUser(user);

        assertNotNull(responseEntity);
        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    void removeUserIfUserDoesNotExists() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertFalse(userService.removeUser(54));
    }

    @Test
    void removeUserIfUserExists() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(new User("John", "Doe")));
        assertTrue(userService.removeUser(54));
    }

    @Test
    void updateUser() {
        User user = new User("John", "Doe");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        User updatedUser = userService.updateUser(5, new User("Miranda", "Hook"));

        assertNotNull(updatedUser);
        assertEquals("Miranda", updatedUser.getFirstName());
        assertEquals("Hook", updatedUser.getLastName());
    }

    @Test
    void retrieveUserBookings() {
//        User user = new User("John", "Doe");
//        when(userRepository.findById(anyInt()))
//                .thenReturn(Optional.of(user));
//
//        List<UserBooking> userBookingList = new ArrayList<>();
//        userBookingList.add(new UserBooking(32, user));
//        userBookingList.add(new UserBooking(53, user));
//
//        when(any(User.class).getBookings()).thenReturn(userBookingList);
//
//        List<UserBooking> mockedList = userService.retrieveUserBookings(123);
//
//        assertNotNull(mockedList);
//        assertEquals(2, mockedList.size());
//        assertEquals(32, mockedList.get(0).getBookingId());
//        assertEquals(54, mockedList.get(1).getBookingId());
    }

    @Test
    void retrieveUserBookingsThrowUserNotFoundException() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(UserNotFoundException.class,
                () -> userService.retrieveUserBookings(32));
    }

    @Test
    void addUserBooking() {
        User user = new User("John", "Doe");
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(user));

        when(userBookingRepository.save(any(UserBooking.class))).thenReturn(new UserBooking(3, user));

        UserBooking userBooking = userService.addUserBooking(94, 3);

        assertNotNull(userBooking);
        assertEquals(3, userBooking.getBookingId());
        assertEquals(0, userBooking.getUserId().getId());
    }

    @Test
    void addUserBookingThrowUserNotFoundException() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(UserNotFoundException.class,
                () -> userService.addUserBooking(32, 8));
    }

    @Test
    void getUserBookings() {
        List<UserBooking> userBookingsList = new ArrayList<>();
        userBookingsList.add(new UserBooking(4, new User("John", "Doe")));
        userBookingsList.add(new UserBooking(8, new User("Miranda", "Wall")));

        when(userBookingRepository.findAllById(any(List.class))).thenReturn(userBookingsList);

        List<Integer> bookingList = new ArrayList<>();

        List<UserBooking> mockedList = userService.getUserBookings(bookingList);

        assertNotNull(mockedList);
        assertEquals(2, mockedList.size());
        assertEquals(4, mockedList.get(0).getBookingId());
        assertEquals(8, mockedList.get(1).getBookingId());
    }
}