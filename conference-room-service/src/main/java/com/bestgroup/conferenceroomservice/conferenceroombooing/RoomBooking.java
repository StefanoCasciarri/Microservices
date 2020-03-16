package com.bestgroup.conferenceroomservice.conferenceroombooing;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class RoomBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

//  TODO: it will look like that after ConferenceRoom is ready
//          but then Dto is needed
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="roomId")
//    private ConferenceRoom conferenceRoom;

    private int userId;
    private Date startDateTime;
    private Date endDateTime;
}
