package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.*;
import com.CareBook.MediSched.Repository.DepartmentRepository;
import com.CareBook.MediSched.Repository.DoctorRequestRepository;
import com.CareBook.MediSched.Service.DoctorRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorRequestServiceImp implements DoctorRequestService {

    private final DoctorRequestRepository doctorRequestRepository;
    private final DepartmentRepository departmentRepository;


    @Override
    public DoctorRequestDto createDoctorRequest(DoctorRequestDto doctorRequestDto, User user) {
        validateRequest(doctorRequestDto);

        Specialty specialty = Specialty.valueOf(doctorRequestDto.getSpecialty().toUpperCase());

        Department department = departmentRepository.findById(doctorRequestDto.getDepartmentId()).orElseThrow(()-> new IllegalArgumentException("This Department not exist"));

        Set<DayOfWeek> days = doctorRequestDto.getDaysOfWeek()
                .stream()
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());


        DoctorRequest doctorRequest =  DoctorRequest.builder()
                .startTime(doctorRequestDto.getStartTime())
                .endTime(doctorRequestDto.getEndTime())
                .status(Status.PENDING)
                .user(user)
                .specialty(specialty)
                .department(department)
                .daysOfWeek(days)
                .build();

        DoctorRequest savedDocRequest = doctorRequestRepository.save(doctorRequest);

        doctorRequestDto.setStatus(savedDocRequest.getStatus().name());
        doctorRequestDto.setId(savedDocRequest.getId());
        return doctorRequestDto;
    }


    private void validateRequest(DoctorRequestDto doctorRequestDto){
        try {
            Specialty.valueOf(doctorRequestDto.getSpecialty().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid specialty: " + doctorRequestDto.getSpecialty());
        }

        for(String day : doctorRequestDto.getDaysOfWeek()) {
            try {
                DayOfWeek.valueOf(day.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid day of week: " + day);
            }
        }

        if(doctorRequestDto.getStartTime() == null || doctorRequestDto.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }

        if(doctorRequestDto.getStartTime().isAfter(doctorRequestDto.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

    }





}
