package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Dto.DoctorDecisionDto;
import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.Doctor;
import com.CareBook.MediSched.Model.User;

public interface DoctorService {

    String decideDoctorRequest(DoctorDecisionDto decisionDto);

    Doctor findByDoctorName(String name);
}
