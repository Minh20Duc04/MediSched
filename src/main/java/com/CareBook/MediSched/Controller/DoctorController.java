package com.CareBook.MediSched.Controller;

import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.DoctorRequest;
import com.CareBook.MediSched.Model.User;
import com.CareBook.MediSched.Service.DoctorRequestService;
import com.CareBook.MediSched.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get-all-requests")
    public ResponseEntity<List<DoctorRequestDto>> getAllDoctorRequests(){
        return ResponseEntity.status(HttpStatus.OK).body(doctorRequestService.getAllDoctorRequests());
    }

    @PutMapping("/decide-request/{doctorRequestId}")
    public ResponseEntity<String> decideDoctorRequest(@PathVariable Long doctorRequestId, String status){
        return ResponseEntity.ok(doctorService.decideDoctorRequest(doctorRequestId, status));
    }


}
