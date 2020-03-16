package com.bestgroup.conferenceroomservice;

import javax.persistence.*;

@Entity
@Table(name="conferenceRooms")
public class ConferenceRoom  {

    @Id
    @Column(name = "roomId")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

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

    public int getId() {
        return id;
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
                "id=" + id +
                ", floor=" + floor +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
