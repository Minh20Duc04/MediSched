package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.DoctorDecisionDto;
import com.CareBook.MediSched.Dto.DoctorDto;
import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.*;
import com.CareBook.MediSched.Repository.*;
import com.CareBook.MediSched.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class DoctorServiceImp implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorRequestRepository doctorRequestRepository;
    private final ScheduleRepository scheduleRepository;
    private final DepartmentRepository departmentRepository;
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
                    .fee(doctorRequest.getFee())
                    .description(doctorRequest.getDescription())
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
    public String updateDoctor(Long doctorId, DoctorRequestDto doctorRequestDto){
        Doctor docDB = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor with this id not found: " + doctorId));

        Department department = departmentRepository.findById(doctorRequestDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Schedule sche = scheduleRepository.findById(docDB.getId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (doctorRequestDto.getSpecialty() != null) {
            String sp = doctorRequestDto.getSpecialty().trim().toUpperCase();
            try {
                docDB.setSpecialty(Specialty.valueOf(sp));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid specialty: " + doctorRequestDto.getSpecialty());
            }
        }

        if (doctorRequestDto.getDaysOfWeek() != null) {
            Set<DayOfWeek> days = doctorRequestDto.getDaysOfWeek().stream()
                    .map(String::toUpperCase)
                    .map(day -> {
                        try {
                            return DayOfWeek.valueOf(day);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("Invalid day of week: " + day);
                        }
                    })
                    .collect(Collectors.toSet());

            sche.setDaysOfWeek(days);
        }

        if (doctorRequestDto.getDepartmentId() != null) {
            docDB.setDepartment(department);
        }

        if (doctorRequestDto.getStartTime() != null && doctorRequestDto.getEndTime() != null) {
            if (doctorRequestDto.getStartTime().isAfter(doctorRequestDto.getEndTime())) {
                throw new IllegalArgumentException("Start time must be before end time");
            }
            sche.setStartTime(doctorRequestDto.getStartTime());
            sche.setEndTime(doctorRequestDto.getEndTime());
        }

        doctorRepository.save(docDB);
        sche.setDoctor(docDB);
        scheduleRepository.save(sche);

        return "Update doctor successfully";
    }

    @Override
    public List<DoctorDto> findByDoctorNameOrSpecialty(String name, String specialty, String page) {
        if(name != null && !name.trim().toLowerCase().isEmpty()){
            name = name.trim().toLowerCase();
        }else {
            name = null;
        }
        if(specialty != null && !specialty.trim().isEmpty()){
            try {
                Specialty.valueOf(specialty);
                specialty = specialty.trim().toUpperCase();
            }catch (IllegalArgumentException e){
                specialty = null;
            }
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("fullName").ascending());

        List<Doctor> doctors = doctorRepository.searchDoctors(name,specialty,pageable).getContent();

        return doctors.stream().map(this::mapToDocDto).collect(Collectors.toList());
    }

    @Override
    public DoctorDto getDoctorProfile(User user) {
        return mapToDocDto(user.getDoctor());
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        return mapToDocDto(doctorRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Doctor not found")));
    }

    private DoctorDto mapToDocDto(Doctor doctor) {
        return new DoctorDto(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getSpecialty().name(),
                doctor.getDepartment(),
                doctor.getUser().getEmail(),
                doctor.getImageUrl(),
                doctor.getFee(),
                doctor.getDescription(),
                doctor.getRole().name());
    }

}
