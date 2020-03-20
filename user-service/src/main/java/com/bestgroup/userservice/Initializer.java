package com.bestgroup.userservice;

import com.bestgroup.userservice.repository.LocalUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Initializer implements CommandLineRunner {

    private PasswordEncoder passwordEncoder;

    private LocalUserRepository userRepository;

    public Initializer(PasswordEncoder passwordEncoder, LocalUserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing app...");
    }
}
