package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.User;

public interface DoctorRequestService {
    DoctorRequestDto createDoctorRequest(DoctorRequestDto doctorRequestDto, User user);
}
