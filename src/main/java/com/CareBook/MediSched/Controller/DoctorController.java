package com.CareBook.MediSched.Controller;

import com.CareBook.MediSched.Dto.DoctorDecisionDto;
import com.CareBook.MediSched.Dto.DoctorDto;
import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.Doctor;
import com.CareBook.MediSched.Model.DoctorRequest;
import com.CareBook.MediSched.Model.User;
import com.CareBook.MediSched.Service.DoctorRequestService;
import com.CareBook.MediSched.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")

public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorRequestService doctorRequestService;

    @PostMapping("/request")
    public ResponseEntity<DoctorRequestDto> createDoctorRequest(
            @RequestParam String specialty,
            @RequestParam List<String> daysOfWeek,
            @RequestParam Long departmentId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam Double fee,
            @RequestParam String description,
            @RequestParam MultipartFile file,
            Authentication authentication){

        User user = (User) authentication.getPrincipal();

        DoctorRequestDto doctorRequestDto = DoctorRequestDto.builder()
                .specialty(specialty)
                .daysOfWeek(daysOfWeek)
                .departmentId(departmentId)
                .fee(fee)
                .description(description)
                .startTime(LocalTime.parse(startTime.replace("\"", "")))
                .endTime(LocalTime.parse(endTime.replace("\"", "")))
                .build();

        return ResponseEntity.ok(doctorRequestService.createDoctorRequest(doctorRequestDto, user, file));
    }

    @GetMapping("/get-all-requests")
    public ResponseEntity<List<DoctorRequestDto>> getAllDoctorRequests(){
        return ResponseEntity.status(HttpStatus.OK).body(doctorRequestService.getAllDoctorRequests());
    }

    @PutMapping("/decide-request")
    public ResponseEntity<String> decideDoctorRequest(@RequestBody DoctorDecisionDto decisionDto){
        return ResponseEntity.ok(doctorService.decideDoctorRequest(decisionDto));
    }

    @PutMapping("/update/{doctorId}")
    public ResponseEntity<String> updateDoctor(@RequestBody DoctorRequestDto doctorRequestDto, @PathVariable(name = "doctorId") Long doctorId){
        return ResponseEntity.ok(doctorService.updateDoctor(doctorId, doctorRequestDto));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorDto>> findByDoctorNameOrSpecialty(@RequestParam(required = false) String name, @RequestParam(required = false) String specialty, @RequestParam(defaultValue = "0") String page){
        return ResponseEntity.ok(doctorService.findByDoctorNameOrSpecialty(name, specialty, page));
    }

    @GetMapping("/me")
    public ResponseEntity<DoctorDto> getDoctorProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(doctorService.getDoctorProfile(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

}
