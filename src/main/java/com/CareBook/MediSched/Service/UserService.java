package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Dto.UserDto;
import com.CareBook.MediSched.Model.User;

import java.util.Map;

public interface UserService {
    User register(UserDto userDto);

    Map<String, Object> authenticateUser(UserDto userDto);


    String sendResetPasswordEmail(String email);
}
