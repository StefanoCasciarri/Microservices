package com.bestgroup.conferenceroomservice.conferenceroombooing;

import com.bestgroup.conferenceroomservice.ConferenceRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Data
public class RoomBooking {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int roomBookingId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomId")
    private ConferenceRoom conferenceRoom;


    @Future(message = "Date should be future")
    private Date startDateTime;

    @Future(message = "Date should be future")
    private Date endDateTime;

    //TODO validate dates, validate room not occupied
}
