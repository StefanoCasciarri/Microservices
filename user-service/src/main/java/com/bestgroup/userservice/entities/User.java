package com.bestgroup.userservice.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name="tuser")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "userId")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @NotNull
    @Setter
    @Size(min=2, max=30)
    private String firstName;

    @NotNull
    @Setter
    @Size(min=2, max=30)
    private String lastName;

    @JsonIgnore
    @OneToMany( fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userId",
              orphanRemoval = true)
    private List<UserBooking> bookings;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", bookings=" + bookings +
                '}';
    }
}
