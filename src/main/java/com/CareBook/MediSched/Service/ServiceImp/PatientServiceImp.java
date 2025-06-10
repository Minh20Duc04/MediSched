package com.CareBook.MediSched.Service.ServiceImp;


import com.CareBook.MediSched.Dto.PatientDto;
import com.CareBook.MediSched.Model.Patient;
import com.CareBook.MediSched.Model.User;
import com.CareBook.MediSched.Repository.PatientRepository;
import com.CareBook.MediSched.Service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImp implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public List<PatientDto> getAllPatients() {
        List<Patient> patientsDB = patientRepository.findAll();
        return patientsDB.stream().map(this::patientToDto).collect(Collectors.toList());
    }

    @Override
    public String updatePatient(User user, Long patientId, PatientDto patientDto) {
        Patient patientDB = patientRepository.findById(patientId).orElseThrow(()-> new IllegalArgumentException("Patient can not found with this id " + patientId));
        if(!user.getRole().name().equals("ADMIN") && !user.getPatient().getId().equals(patientDB.getId())){
            throw new RuntimeException("You don't have permission to update other information");
        }
        patientDB.setFirstName(patientDto.getFirstName());
        patientDB.setLastName(patientDto.getLastName());
        patientDB.setEmail(patientDto.getEmail());
        patientDB.setUsername(patientDto.getUsername());
        patientDB.setPhoneNumber(patientDto.getPhoneNumber());
        patientDB.setAddress(patientDto.getAddress());
        patientDB.setDob(patientDto.getDob());

        patientRepository.save(patientDB);
        return "Update Patient successfully";
    }

    @Override
    public String deletePatient(Long patientId) {
        Patient patientDB = patientRepository.findById(patientId).orElseThrow(()-> new IllegalArgumentException("Can not found patient with this id " + patientId));
        patientRepository.delete(patientDB);
        return "Delete patient successfully";
    }

    @Override
    public PatientDto getPatientProfile(User user) {
        return patientToDto(user.getPatient());
    }


    private PatientDto patientToDto(Patient patient) {
        return new PatientDto(patient.getFirstName(), patient.getLastName(), patient.getEmail(), patient.getUsername(), patient.getPhoneNumber(), patient.getAddress(), patient.getDob());
    }

}
