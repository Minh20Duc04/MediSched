package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.UserDto;
import com.CareBook.MediSched.Model.Role;
import com.CareBook.MediSched.Model.User;
import com.CareBook.MediSched.Repository.UserRepository;
import com.CareBook.MediSched.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public User register(UserDto userDto) {
        User user = mapToUser(userDto);
        return userRepository.save(user);
    }

    @Override
    public Map<String, Object> authenticateUser(UserDto userDto) {
        Map<String, Object> authenticatedUser = new HashMap<>();
        User user =(User) userDetailsService.loadUserByUsername(userDto.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        authenticatedUser.put("token", "Bearer".concat(jwtService.generateToken(userDto.getUsername())));
        authenticatedUser.put("user", user);
        return authenticatedUser;
    }


    private User mapToUser(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .address(userDto.getAddress())
                .dob(userDto.getDob())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .role(Role.USER)
                .build();
    }



}
