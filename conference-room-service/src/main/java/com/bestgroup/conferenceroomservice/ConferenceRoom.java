package com.bestgroup.conferenceroomservice;

import javax.persistence.*;
import javax.validation.constraints.*;

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

    public ConferenceRoom() {
    }

    public ConferenceRoom(int floor,String name,int size){
        this.floor = floor;
        this.name = name;
        this.size = size;
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
