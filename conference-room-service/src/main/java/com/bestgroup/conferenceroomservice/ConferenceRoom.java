package com.bestgroup.conferenceroomservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ConferenceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @Override
    public String toString() {
        return "ConferenceRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public ConferenceRoom() {}

    public ConferenceRoom(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
