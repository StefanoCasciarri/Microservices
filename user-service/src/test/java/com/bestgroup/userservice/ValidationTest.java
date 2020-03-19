package com.bestgroup.userservice;


import com.bestgroup.userservice.entities.User;
import com.bestgroup.userservice.entities.UserBooking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ValidationTest {

    private static Validator validator;
    private static ValidatorFactory validatorFactory;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void validUser() {
        User validUser = new User("John", "Doe");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void invalidUserShortFirstName() {
        User inValidUser = new User("J", "Doe");
        Set<ConstraintViolation<User>> violations = validator.validate(inValidUser);
        assertEquals(1, violations.size());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("size must be between 2 and 30", violation.getMessage());

    }

    @Test
    public void invalidUserShortLastName() {
        User inValidUser = new User("John", "D");
        Set<ConstraintViolation<User>> violations = validator.validate(inValidUser);
        assertEquals(1, violations.size());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("size must be between 2 and 30", violation.getMessage());

    }

    @Test
    public void invalidUserLongFirstName() {
        User inValidUser =
                new User("Jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", "Doe");
        Set<ConstraintViolation<User>> violations = validator.validate(inValidUser);
        assertEquals(1, violations.size());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("size must be between 2 and 30", violation.getMessage());

    }

    @Test
    public void invalidUserLongLastName() {
        User inValidUser = new User("John", "Ddddddddddddddddddddddddddddddd");
        Set<ConstraintViolation<User>> violations = validator.validate(inValidUser);
        assertEquals(1, violations.size());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("size must be between 2 and 30", violation.getMessage());

    }

    @Test
    public void invalidUserNullNames() {
        User inValidUser = new User();
        Set<ConstraintViolation<User>> violations = validator.validate(inValidUser);
        assertEquals(2, violations.size());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("must not be null", violation.getMessage());
        violation = violations.iterator().next();
        assertEquals("must not be null", violation.getMessage());

    }

    @Test
    public void validUserBooking() {
        UserBooking validUserBooking = new UserBooking(4, new User("John", "Doe"));
        Set<ConstraintViolation<UserBooking>> violations = validator.validate(validUserBooking);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void invalidUserBookingBookingIdNotPositive() {
        UserBooking validUserBooking = new UserBooking(-1, new User("John", "Doe"));
        Set<ConstraintViolation<UserBooking>> violations = validator.validate(validUserBooking);
        assertEquals(1, violations.size());

        ConstraintViolation<UserBooking> violation = violations.iterator().next();
        assertEquals("must be greater than 0", violation.getMessage());
    }

    @Test
    public void invalidUserBookingUserNull() {
        UserBooking validUserBooking = new UserBooking(4, null);
        Set<ConstraintViolation<UserBooking>> violations = validator.validate(validUserBooking);
        assertEquals(1, violations.size());

        ConstraintViolation<UserBooking> violation = violations.iterator().next();
        assertEquals("must not be null", violation.getMessage());
    }


}
