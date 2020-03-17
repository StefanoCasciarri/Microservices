package com.bestgroup.userservice;

import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookingRepository extends JpaRepository<UserBooking, Integer> {


}
