package com.bestgroup.conferenceroomservice;

import com.bestgroup.conferenceroomservice.conferenceroombooing.RoomBooking;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="conferenceRooms")
@NoArgsConstructor
@Getter
public class ConferenceRoom  {

    @Id
    @Column(name = "roomId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int roomId;

    @NotNull(message = "Field must not be empty.")
    @Min(value=0)
    @Max(value=50)
    @Setter
    private Integer floor;

    @NotBlank(message = "Field must not be empty.")
    @Size(min=2,max=30)
    @Setter
    private String name;

    @NotNull(message = "Field must not be empty.")
    @Min(value=1)
    @Max(value=50)
    @Setter
    private Integer size;

    @JsonIgnore
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "conferenceRoom")
    @Setter
    private List<RoomBooking> roomBookings;


    public ConferenceRoom(int floor,String name,int size){
        this.floor = floor;
        this.name = name;
        this.size = size;
        this.roomBookings = new ArrayList<>();
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
