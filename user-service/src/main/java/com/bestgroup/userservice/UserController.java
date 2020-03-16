package com.bestgroup.userservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
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
    public void createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.get();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable int id, @RequestBody User updatedUser) {
            userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName((updatedUser.getLastName()));
                    return userRepository.save(user);
                });

    }

}
