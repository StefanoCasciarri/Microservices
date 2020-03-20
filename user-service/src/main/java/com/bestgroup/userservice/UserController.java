package com.bestgroup.userservice;


import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
import com.bestgroup.userservice.responseentitystructure.RoomBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService=service;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.retrieveAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<Object> postUser(@Valid @RequestBody User user) {
        return userService.newUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.retrieveUser(id);
    }

    @DeleteMapping("/users/{id}")
    public boolean deleteUser(@PathVariable int id) {
        return userService.removeUser(id);
    }

    @PutMapping("/users/{id}")
    public User putUser(@PathVariable int id, @Valid @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @GetMapping("/users/{userId}/bookings")
    public List<RoomBooking> getUserRoomBookings(@PathVariable int userId) {
        return userService.retrieveUserBookings(userId);
    }

    @PostMapping("/users/{id}/bookings")
    public ResponseEntity<UserBooking> addUserBooking(@PathVariable int id, @RequestParam int bookingID) {
        UserBooking booking = userService.addUserBooking( id, bookingID);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping("users/bookings/")
    public List<UserBooking> getUserBookings(@RequestParam List<Integer> bookings){
        return userService.getUserBookings(bookings);
    }


}
