package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Repository.DoctorRepository;
import com.CareBook.MediSched.Repository.DoctorRequestRepository;
import com.CareBook.MediSched.Repository.ScheduleRepository;
import com.CareBook.MediSched.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class DoctorServiceImp implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorRequestRepository doctorRequestRepository;
    private final ScheduleRepository scheduleRepository;






}
