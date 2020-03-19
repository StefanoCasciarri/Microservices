package com.bestgroup.userservice.responseentitystructure;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConferenceRoom {

    private int roomId;
    private Integer floor;
    private String name;
    private Integer size;

    public ConferenceRoom(int floor, String name, int size){
        this.floor = floor;
        this.name = name;
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
