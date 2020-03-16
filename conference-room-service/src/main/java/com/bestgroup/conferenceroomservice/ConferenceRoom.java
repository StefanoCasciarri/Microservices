package com.bestgroup.conferenceroomservice;

import javax.persistence.*;

@Entity
@Table(name="conferenceRooms")
public class ConferenceRoom  {

    @Id
    @Column(name = "roomId")
    @GeneratedValue(strategy=GenerationType.AUTO)
    int roomId;

    @Column(name = "floor")
    int floor;

    @Column(name ="name")
    String name;

    @Column(name ="size")
    int size;

    public ConferenceRoom() {
    }

    public ConferenceRoom(int floor,String name,int size){
        this.floor = floor;
        this.name = name;
        this.size = size;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getNumber() {
        return name;
    }

    public void setNumber(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
