package com.bestgroup.conferenceroomservice.responseentitystructure;

import lombok.Data;

@Data
public class User {

    private int id; //has to be id not userId, does not work with UserMS when userId
    private String firstName;
    private String lastName;

}
