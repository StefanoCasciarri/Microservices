package com.bestgroup.conferenceroomservice;

import com.bestgroup.conferenceroomservice.conferenceroombooing.RoomBooking;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="conferenceRooms")
public class ConferenceRoom  {

    @Id
    @Column(name = "roomId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int roomId;

    @NotNull(message = "Field must not be empty.")
    @Min(value=0)
    @Max(value=50)
    @Column(name = "floor")
    private Integer floor;

    @NotBlank(message = "Field must not be empty.")
    @Size(min=2,max=30)
    @Column(name ="name")
    private String name;

    @NotNull(message = "Field must not be empty.")
    @Min(value=1)
    @Max(value=50)
    @Column(name ="size")
    private Integer size;

    @JsonIgnore
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)//, mappedBy = "conferenceRoom"
    private List<RoomBooking> roomBookings;

    public ConferenceRoom() {
    }

    public ConferenceRoom(int floor,String name,int size){
        this.floor = floor;
        this.name = name;
        this.size = size;
        this.roomBookings = new ArrayList<>();
    }

    public int getRoomId() {
        return roomId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<RoomBooking> getRoomBookings() {
        return roomBookings;
    }

    @Override
    public String toString() {
        return "ConferenceRoom{" +
                "id=" + roomId +
                ", floor=" + floor +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
