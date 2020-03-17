package com.bestgroup.userservice;


import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
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
    public List<User> retrieveAllUsers() {
          return userRepository.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()) {
            throw new UserNotFoundException("id: " + userId);
        }

        return optionalUser.get();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int userId) {
        userRepository.deleteById(userId);
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable int userId, @Valid @RequestBody User updatedUser) {
            userRepository.findById(userId)
                .map(user -> {
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName((updatedUser.getLastName()));
                    return userRepository.save(user);
                });

    }

    @GetMapping("/users/{id}/bookings")
    public List<UserBooking> retrieveUserBookings(@PathVariable int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        //TODO communicate with second microservice and retrieving booking information
        return optionalUser.get().getBookings();
    }
    //TODO restpoint for other service to add bookings for persons

    @PostMapping("/users/{id}/bookings")
    public UserBooking addUserBooking(@PathVariable int userId, @RequestParam int bookingID) {


        Optional<User> optionalUser = userRepository.findById(userId);
            if(!optionalUser.isPresent()) {
                throw new UserNotFoundException("id: " + id);
            }

        UserBooking booking = new UserBooking(bookingID,optionalUser.get());
        //TODO communicate with second microservice and retrieving booking information
        return optionalUser.get().getBookings();
    }
}
