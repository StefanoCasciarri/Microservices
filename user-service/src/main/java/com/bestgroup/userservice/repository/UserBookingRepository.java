package com.bestgroup.userservice.repository;

import com.bestgroup.userservice.entities.UserBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookingRepository extends JpaRepository<UserBooking, Integer> {


}
