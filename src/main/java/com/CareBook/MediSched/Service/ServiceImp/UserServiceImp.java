package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.UserDto;
import com.CareBook.MediSched.Model.Role;
import com.CareBook.MediSched.Model.User;
import com.CareBook.MediSched.Repository.UserRepository;
import com.CareBook.MediSched.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

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

    @Override
    public String sendResetPasswordEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Can not find your email"));

        String newPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        sendEmail(email, newPassword);
        return "A new password has been sent to your email.";
    }

    private void sendEmail(String email, String newPassword){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("MediSched - Password Reset");
        message.setText("Your new password is: " + newPassword + "\nPlease change it after logging in.");
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    private String generateRandomPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = (int)(Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
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
