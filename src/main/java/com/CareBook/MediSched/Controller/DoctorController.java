package com.CareBook.MediSched.Controller;

import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.DoctorRequest;
import com.CareBook.MediSched.Model.User;
import com.CareBook.MediSched.Service.DoctorRequestService;
import com.CareBook.MediSched.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")

public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorRequestService doctorRequestService;

    @PostMapping("/request")
    public ResponseEntity<DoctorRequestDto> createDoctorRequest(@RequestBody DoctorRequestDto doctorRequestDto , Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(doctorRequestService.createDoctorRequest(doctorRequestDto, user));
    }


}
