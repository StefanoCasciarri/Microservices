package com.bestgroup.conferenceroomservice;

import javax.persistence.*;

@Entity
@Table(name="conferenceRooms")
public class ConferenceRoom  {

    @Id
    @Column(name = "roomId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int roomId;

    @Column(name = "floor")
    private int floor;

    @Column(name ="name")
    private String name;

    @Column(name ="size")
    private int size;

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
