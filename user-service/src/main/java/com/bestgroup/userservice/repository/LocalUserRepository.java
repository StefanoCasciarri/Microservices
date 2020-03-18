package com.bestgroup.userservice.repository;

import com.bestgroup.userservice.entities.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalUserRepository extends JpaRepository<LocalUser, Integer> {

    public Optional<LocalUser> findByLogin(String login);
}
