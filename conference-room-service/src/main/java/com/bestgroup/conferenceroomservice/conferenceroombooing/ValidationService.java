package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoom;
import com.bestgroup.conferenceroomservice.ConferenceRoomRepository;
import com.bestgroup.conferenceroomservice.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidationService {
    private RoomBookingRepository roomBookingRepository;
    private ConferenceRoomRepository conferenceRoomRepository;


    @Autowired
    public ValidationService(RoomBookingRepository roomBookingRepository,
                              ConferenceRoomRepository conferenceRoomRepository) {
        this.roomBookingRepository = roomBookingRepository;
        this.conferenceRoomRepository = conferenceRoomRepository;

    }
    public boolean isRoomExist(Integer roomId){
        Optional<ConferenceRoom> roomBookings = conferenceRoomRepository.findById(roomId);
        if(roomBookings.isPresent()){
            return true;
        }
        else throw new ResourceNotFoundException("No such room.");
    }
    public boolean isDurationValid(RoomBooking roomBooking){
        if(roomBooking.getStartDateTime().compareTo(roomBooking.getEndDateTime()) < 0){
            return true;
        }
        else throw new BadRequestExeption("End date before start date");
    }

    public boolean isRoomAvailable(Integer roomId, RoomBooking newRoomBooking){
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.findById(roomId);
        if(conferenceRoom.isPresent()){
            List<RoomBooking> roomBookings  = conferenceRoom.get().getRoomBookings();
            for(RoomBooking roomBooked: roomBookings){
                if((    roomBooked.getStartDateTime().compareTo(newRoomBooking.getStartDateTime()) < 0 &&
                        roomBooked.getEndDateTime().compareTo(newRoomBooking.getStartDateTime()) > 0) ||

                       (roomBooked.getStartDateTime().compareTo(newRoomBooking.getEndDateTime()) < 0 &&
                        roomBooked.getEndDateTime().compareTo(newRoomBooking.getEndDateTime()) > 0) ||

                       (roomBooked.getStartDateTime().compareTo(newRoomBooking.getStartDateTime()) > 0 &&
                        roomBooked.getEndDateTime().compareTo(newRoomBooking.getEndDateTime()) < 0) ||

                       (roomBooked.getStartDateTime().compareTo(newRoomBooking.getStartDateTime()) < 0 &&
                        roomBooked.getEndDateTime().compareTo(newRoomBooking.getEndDateTime()) > 0)||

                        (roomBooked.getStartDateTime().compareTo(newRoomBooking.getStartDateTime()) == 0 &&
                                roomBooked.getEndDateTime().compareTo(newRoomBooking.getEndDateTime()) == 0)){

                    throw new BadRequestExeption("This room is occupied at this time");
                }
            }
        }
        return true;
    }
}
