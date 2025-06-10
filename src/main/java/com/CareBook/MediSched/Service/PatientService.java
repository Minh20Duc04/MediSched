package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Dto.PatientDto;
import com.CareBook.MediSched.Model.User;

import java.util.List;

public interface PatientService {

    List<PatientDto> getAllPatients();

    String updatePatient(User user, Long patientId, PatientDto patientDto);

    String deletePatient(Long patientId);

    PatientDto getPatientProfile(User user);
}
