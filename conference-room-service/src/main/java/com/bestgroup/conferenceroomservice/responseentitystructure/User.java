package com.bestgroup.conferenceroomservice.responseentitystructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id; //has to be id not userId, does not work with UserMS when userId
    private String firstName;
    private String lastName;

}
