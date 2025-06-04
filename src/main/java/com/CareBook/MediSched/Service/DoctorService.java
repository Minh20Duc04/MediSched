package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Dto.DoctorDecisionDto;
import com.CareBook.MediSched.Dto.DoctorDto;
import com.CareBook.MediSched.Dto.DoctorRequestDto;

import java.util.List;

public interface DoctorService {

    String decideDoctorRequest(DoctorDecisionDto decisionDto);

    String updateDoctor(Long doctorId, DoctorRequestDto doctorRequestDto);

    List<DoctorDto> findByDoctorNameOrSpecialty(String name, String specialty, String page);
}
