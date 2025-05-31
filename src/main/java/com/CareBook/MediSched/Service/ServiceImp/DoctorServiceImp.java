package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.DoctorDecisionDto;
import com.CareBook.MediSched.Model.*;
import com.CareBook.MediSched.Repository.DoctorRepository;
import com.CareBook.MediSched.Repository.DoctorRequestRepository;
import com.CareBook.MediSched.Repository.ScheduleRepository;
import com.CareBook.MediSched.Repository.UserRepository;
import com.CareBook.MediSched.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@RequiredArgsConstructor

public class DoctorServiceImp implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorRequestRepository doctorRequestRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;


    @Override
    public String decideDoctorRequest(DoctorDecisionDto decisionDto){
        DoctorRequest doctorRequest = doctorRequestRepository.findById(decisionDto.getDoctorRequestId()).orElseThrow(()-> new RuntimeException("Can't find this doctor request"));
        Status seekStatus;
        try{
            seekStatus = Status.valueOf(decisionDto.getStatus());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid status");
        }

        if(seekStatus.equals(Status.APPROVED)){
            boolean checkExistedDoc = doctorRepository.existsByUserId(doctorRequest.getUser().getId());
            if(checkExistedDoc){
                throw new IllegalStateException("User is already a doctor");
            }

            Doctor createDoctor = Doctor.builder()
                    .fullName(doctorRequest.getUser().getFirstName() + " "+doctorRequest.getUser().getLastName())
                    .specialty(doctorRequest.getSpecialty())
                    .role(Role.DOCTOR)
                    .department(doctorRequest.getDepartment())
                    .user(doctorRequest.getUser())
                    .imageUrl(doctorRequest.getImageUrl())
                    .build();
            doctorRepository.save(createDoctor);

            doctorRequest.setStatus(Status.APPROVED);
            doctorRequestRepository.save(doctorRequest);

            User user = userRepository.findById(doctorRequest.getUser().getId()).orElseThrow();
            user.setRole(createDoctor.getRole());
            userRepository.save(user);

            Schedule createSchedule = Schedule.builder()
                    .doctor(createDoctor)
                    .daysOfWeek(new HashSet<>(doctorRequest.getDaysOfWeek()))
                    .startTime(doctorRequest.getStartTime())
                    .endTime(doctorRequest.getEndTime())
                    .build();

            scheduleRepository.save(createSchedule);
            return "Doctor request approved successfully";
        }
        else if(seekStatus == Status.PENDING){
            return "Doctor request is still pending";
        }
        else{
            doctorRequest.setStatus(Status.REJECTED);
            doctorRequestRepository.save(doctorRequest);
            return "Doctor request rejected.";
        }
    }

    @Override
    public Doctor findByDoctorName(String name) {
        Doctor docDB = doctorRepository.findByFullName(name).orElseThrow(()-> new IllegalArgumentException("Can not find doctor by "+name));
        return docDB;
    }
}
