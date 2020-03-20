package com.bestgroup.conferenceroomservice;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTest {


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
    public void validateFloorFieldLowerBound() {

        ConferenceRoom conferenceRoom = new ConferenceRoom(-1,"TEST",10);
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"must be greater than or equal to 0");

    }


    @Test
    public void validateFloorFieldUpperBound() {

        ConferenceRoom conferenceRoom = new ConferenceRoom(51,"TEST",10);
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"must be less than or equal to 50");

    }

    @Test
    public void validateFloorFieldIsNotNull() {

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setSize(15);
        conferenceRoom.setName("TEST");
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"Field must not be empty.");

    }

    @Test
    public void validateNameFieldLowerBound() {

        ConferenceRoom conferenceRoom = new ConferenceRoom(1,"T",10);
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"size must be between 2 and 30");

    }

    @Test
    public void validateNameFieldUpperBound() {

        ConferenceRoom conferenceRoom = new ConferenceRoom(1,"0123456789012345678901234567890",10);
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"size must be between 2 and 30");

    }

    @Test
    public void validateNameFieldIsNotNull() {

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setSize(15);
        conferenceRoom.setFloor(1);
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"Field must not be empty.");

    }

    /**
     Unambiguous behavior of validator.validate(conferenceRoom) in validateNameFieldIsNotEmpty test.
     In some runs message extracted of violation = Field must not be empty
     in other runs message extracted of violation = size must be between 2 and 30
     using the same input data.
     */

    @Test
    public void validateNameFieldIsNotEmpty() {

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setSize(15);
        conferenceRoom.setFloor(1);
        conferenceRoom.setName("");
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        System.out.println(message);
        assertTrue(message.equals("size must be between 2 and 30") ||
        message.equals("Field must not be empty."));

    }

    @Test
    public void validateNameFieldIsNotBlank() {

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setSize(15);
        conferenceRoom.setFloor(1);
        conferenceRoom.setName("   ");
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"Field must not be empty.");

    }

    @Test
    public void validateSizeFieldLowerBound() {

        ConferenceRoom conferenceRoom = new ConferenceRoom(1,"TEST",0);
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"must be greater than or equal to 1");

    }


    @Test
    public void validateSizeFieldUpperBound() {

        ConferenceRoom conferenceRoom = new ConferenceRoom(1,"TEST",51);
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"must be less than or equal to 50");

    }

    @Test
    public void validateSizeFieldIsNotNull() {

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setFloor(1);
        conferenceRoom.setName("TEST");
        Set<ConstraintViolation<ConferenceRoom>> violations = validator.validate(conferenceRoom);

        String message = "";
        for (ConstraintViolation<ConferenceRoom> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals(message,"Field must not be empty.");

    }

}
