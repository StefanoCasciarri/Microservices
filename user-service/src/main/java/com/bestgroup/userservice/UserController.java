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

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public User retrieveUser(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()) {
            throw new UserNotFoundException("id: " + id);
        }

        return optionalUser.get();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable int id, @Valid @RequestBody User updatedUser) {
            userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName((updatedUser.getLastName()));
                    return userRepository.save(user);
                });

    }

    @GetMapping("/users/{id}/bookings")
    public List<UserBookings> retrieveUserBookings(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        //TODO communicate with second microservice and retrieving booking information
        return optionalUser.get().getBookings();
    }
    //TODO restpoint for other service to add bookings for persons 
}
