package com.mohit.financeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Data

@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequest {

    private String firstName;
    private String secondName; 
    private String otherName;
    private String email;
    private String gender;
    private String phoneNumber;
    private String address;
    private String status;
    private String password;
}
