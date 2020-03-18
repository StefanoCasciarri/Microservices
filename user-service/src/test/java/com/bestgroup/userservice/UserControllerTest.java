package com.bestgroup.userservice;

import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.print.attribute.ResolutionSyntax;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
    void getAllUsers() {

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
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("/1",responseEntity.getHeaders().getLocation().getPath());

    }

    @Test
    void getUser() {

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
    void getUserBookings() {

        List<UserBooking> userBookings = new ArrayList<>();
        userBookings.add(new UserBooking(3,new User("John","Doe")));
        userBookings.add(new UserBooking(8,new User("Miranda","Wall")));

        when(userService.retrieveUserBookings(anyInt())).thenReturn(userBookings);

        List<UserBooking> mockedList = userController.getUserBookings(33);

        assertNotNull(mockedList);
        assertEquals(2, mockedList.size());
        assertEquals(3, mockedList.get(0).getBookingId());
        assertEquals(8, mockedList.get(1).getBookingId());

    }

    @Test
    void addUserBooking() {
        UserBooking userBooking = new UserBooking(5, new User("John", "Doe"));
        when(userService.addUserBooking(anyInt(),anyInt())).thenReturn(userBooking);

        ResponseEntity<UserBooking> responseEntity = userController.addUserBooking(5, new User("John", "Doe"));
    }

    @Test
    void testGetUserBookings() {
    }
}