package com.bestgroup.userservice;


import com.bestgroup.userservice.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
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

        assertEquals("John", mockedUser.getFirstName());
        assertEquals("Doe", mockedUser.getLastName());
    }


    @Test
    void retrieveUserThrowUserNotFoundException() {
        Optional<User> userOptional = Optional.ofNullable(null);
        when(userRepository.findById(anyInt())).thenReturn(userOptional);

        assertThrows(UserNotFoundException.class, () -> userService.retrieveUser(33));
    }

    @Test
    void newUser() {
//        User user = new User("John", "Doe");
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        ResponseEntity<Object> responseEntity = userService.newUser(user);
//
//
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals("/1",responseEntity.getHeaders().getLocation().getPath());
    }

    @Test
    void removeUserIfUserDoesNotExists() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertFalse(userService.removeUser(54));
    }

    @Test
    void removeUserIfUserExists() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(new User("John", "Doe")));
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
    void retrieveUserBookingsThrowUserNotFoundException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));

        assertThrows(UserNotFoundException.class, () -> userService.retrieveUserBookings(32));

    }

    @Test
    void addUserBooking() {
    }

    @Test
    void getUserBookings() {
    }
}