package com.bestgroup.userservice;

import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
import com.bestgroup.userservice.responseentitystructure.RoomBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserBookingRepository bookingRepository;
    private RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserBookingRepository bookingRepository,
                       RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = restTemplate;
    }

    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<Object> newUser(User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    public User retrieveUser(int id) {
        Optional<User> userWrappedInOptional = userRepository.findById(id);
        userWrappedInOptional.orElseThrow(() -> new UserNotFoundException("id: " + id));

        return userWrappedInOptional.get();
    }

    public boolean removeUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()) {
            return false;
        }

        userRepository.deleteById(id);
        return true;
    }

    public User updateUser(int id, User updatedUser) {
        userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName((updatedUser.getLastName()));
                    userRepository.save(user);
                    return user;
                });
        return updatedUser;
    }

    public List<RoomBooking> retrieveUserBookings(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) throw new UserNotFoundException("id: " + userId);//check if user Exixts

        List<UserBooking> userBookings = user.get().getBookings();
        List<RoomBooking>  roomBookings = retrieveRoomBookings(userBookings);

        //delete those UserBookings that were lost in Room Microservice
        deleteLostUserBookings(userBookings, roomBookings);

        return roomBookings; // may be empty, if bookingId not found then not shown
    }

    //retrieve room bookings form Room Microservice
    private List<RoomBooking> retrieveRoomBookings(List<UserBooking> userBookings) {

        List<Integer> bookingsIds = retrieveUserBookingsIds(userBookings);
        String uri = createUriCall(bookingsIds);
        RoomBooking[] userBookingsArray= restTemplate.getForObject(uri,  RoomBooking[].class);
        List<RoomBooking>  roomBookings = Arrays.asList(userBookingsArray);
        return roomBookings;
    }

    private List<Integer> retrieveUserBookingsIds(List<UserBooking> userBookings) {

        List<Integer> bookingsIds = new ArrayList<>();// list of bookings Ids
        userBookings.forEach(userBooking -> bookingsIds.add(userBooking.getBookingId()));
        return bookingsIds;
    }

    private String createUriCall(List<Integer> bookingsIds) {

        String uri = new String("http://localhost:8070/conference-rooms/bookings/");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(uri)
                .queryParam("bookings", bookingsIds);
        return  builder.toUriString();
    }

    //delete those userBookings that their id can not be found in roomBookings
    private List<UserBooking> deleteLostUserBookings(List<UserBooking> userBookings, List<RoomBooking> roomBookings) {

        List<UserBooking> lostUserBookings = findLostUserBookings(userBookings, roomBookings);

        userBookings.removeAll(lostUserBookings); //remove lost bookings from collection in user
        bookingRepository.deleteAll(lostUserBookings); //remove lost bookings from repository

        return userBookings;
    }

    //find those userBookings that their id can not be found in roomBookings
    private List<UserBooking> findLostUserBookings(List<UserBooking> userBookings, List<RoomBooking> roomBookings) {

        List<UserBooking> lostUserBookings = new ArrayList<>();
        for(UserBooking userBooking: userBookings ){
            Boolean bookingIdIsLost = checkIfLostUserBooking(userBooking, roomBookings);

            if(bookingIdIsLost){
                lostUserBookings.add(userBooking); //add userBooking that wasnt fount in roomBookings to lostUserBookings
            }
        }
        return lostUserBookings;
    }

    //check if userBooking is present in roomBookings if not then its lost
    private Boolean checkIfLostUserBooking(UserBooking userBooking, List<RoomBooking> roomBookings) {

        Boolean bookingIdIsLost = true;

        for(RoomBooking roomBooking: roomBookings){
            if(roomBooking.getRoomBookingId()==userBooking.getBookingId()) {
                bookingIdIsLost = false; //userBooking found in roomBooking
                break;
            }
        }
        return  bookingIdIsLost;
    }

    public UserBooking addUserBooking(int userId, int bookingId ){
       Optional<User> optionalUser = userRepository.findById(userId);
       if(!optionalUser.isPresent()) {
           throw new UserNotFoundException("id: " + userId);
       }
       return bookingRepository.save(new UserBooking(bookingId,optionalUser.get()));
    }

    public List<UserBooking> getUserBookings(List<Integer> bookings) {
        return  bookingRepository.findAllById(bookings);
    }
}
