package com.bestgroup.userservice;

import com.bestgroup.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select distinct u from User u left join fetch u.bookings where u.userId = :userId")
    Optional<User> findByUserIdWithBookings(int userId);
}
