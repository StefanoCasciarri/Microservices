package com.bestgroup.userservice;


import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBookings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private UserRepository userRepository;
    private UserBookingRepository bookingRepository;

    @Autowired
    public UserController(UserRepository userRepository, UserBookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
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
    public void putUser(@PathVariable int id, @Valid @RequestBody User updatedUser) {
        userService.updateUser(id, updatedUser);
    }

    @GetMapping("/users/{id}/bookings")
    public List<UserBooking> getUserBookings(@PathVariable int id) {
        return userService.retrieveUserBookings(id);
    }
    //TODO restpoint for other service to add bookings for persons 
}
