package com.CareBook.MediSched.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String firstName;

    private String lastName;

    private String username;

    private Date dob;

    private String email;

    private String password;

    private Long phoneNumber;

    private String address;

}
