package com.CareBook.MediSched.Controller;

import com.CareBook.MediSched.Dto.PatientDto;
import com.CareBook.MediSched.Model.User;
import com.CareBook.MediSched.Repository.PatientRepository;
import com.CareBook.MediSched.Service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;


    @GetMapping("/get-all")
    public ResponseEntity<List<PatientDto>> getAllPatients(){
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("update/{patientId}")
    public ResponseEntity<String> updatePatient(@PathVariable Long patientId, @RequestBody PatientDto patientDto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(patientService.updatePatient(user ,patientId, patientDto));
    }

    @DeleteMapping("delete/{patientId}")
    public ResponseEntity<String> deletePatient(@PathVariable Long patientId){
        return ResponseEntity.ok(patientService.deletePatient(patientId));
    }

    @GetMapping("/me")
    public ResponseEntity<PatientDto> getPatientProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(patientService.getPatientProfile(user));
    }
}
