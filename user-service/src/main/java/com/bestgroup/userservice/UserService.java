package com.bestgroup.userservice;

import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
import com.bestgroup.userservice.repository.UserBookingRepository;
import com.bestgroup.userservice.repository.UserRepository;
import com.bestgroup.userservice.responseentitystructure.RoomBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
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
                .buildAndExpand(savedUser.getUserId()).toUri();

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
        Optional<User> userAfterChange = userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName((updatedUser.getLastName()));
                    userRepository.save(user);
                    return user;
                });
        if(!userAfterChange.isPresent()) {
            throw new UserNotFoundException("id: " + id);
        }
        return userAfterChange.get();
    }

    public List<RoomBooking> retrieveUserBookings(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) throw new UserNotFoundException("id: " + userId);

        List<Integer> bookingsIds = new ArrayList<>();// list of bookings Ids
        List<UserBooking> userBookings = user.get().getBookings();
        userBookings.forEach(userBooking -> bookingsIds.add(userBooking.getBookingId()));

        String uri = new String("http://localhost:8070/conference-rooms/bookings/");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(uri)
                .queryParam("bookings", bookingsIds);

        HttpEntity entity = this.createTokenHeader();
        ResponseEntity<RoomBooking[]> userBookingsArray;
        userBookingsArray = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, RoomBooking[].class);
        List<RoomBooking>  roomBookings = Arrays.asList(userBookingsArray.getBody());
        //delete those UserBookings that were lost in Room Microservice
        deleteLostUserBookings(userBookings, roomBookings);

        return roomBookings; // may be empty, if bookingId not found then not shown
    }

    private List<UserBooking> deleteLostUserBookings(List<UserBooking> userBookings, List<RoomBooking> roomBookings) {
        //delete those userBookings that their id can not be found in roomBookings

        List<UserBooking> lostUserBookings = new ArrayList<>();
        for(UserBooking userBooking: userBookings ){
            Boolean bookingIdIsLost = true;
            for(RoomBooking roomBooking: roomBookings){
                if(roomBooking.getRoomBookingId()==userBooking.getBookingId()) {
                    bookingIdIsLost = false;
                    break;
                }
            }
            if(bookingIdIsLost){
                lostUserBookings.add(userBooking);
            }
        }
        userBookings.removeAll(lostUserBookings);
        bookingRepository.deleteAll(lostUserBookings);
        return userBookings;

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



    private String getTokenFromRequest(){
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder
                        .currentRequestAttributes())
                .getRequest();
        String value = request.getHeader("Authorization").split(" ")[1];
        return value;
    }

    private HttpEntity createTokenHeader(){
        String token = this.getTokenFromRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity(headers);
    }
}
