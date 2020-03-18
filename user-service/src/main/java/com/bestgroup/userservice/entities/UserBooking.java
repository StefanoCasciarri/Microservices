package com.bestgroup.userservice.entities;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name="tuserbooking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "bookingId")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserBooking {

    @Id
    @Positive
    private int bookingId;
    @NotNull
    @ManyToOne
    private User userId;




    @Override
    public String toString() {
        return "UserBookings{" +
                "bookingId=" + bookingId +
                ", userId=" + userId.getUserId() +
                '}';
    }
}
