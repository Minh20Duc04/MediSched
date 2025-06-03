package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.AppointmentDto;
import com.CareBook.MediSched.Model.*;
import com.CareBook.MediSched.Repository.*;
import com.CareBook.MediSched.Service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImp implements AppointmentService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    @Override
    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor docDB = doctorRepository.findById(doctorId).orElseThrow(()-> new IllegalArgumentException());
        Schedule schedDB = scheduleRepository.findByDoctorAndDaysOfWeekContaining(docDB, date.getDayOfWeek());

        List<LocalTime> availableSlots  = new ArrayList<>();
        LocalTime startTime = schedDB.getStartTime();

        while (startTime.isBefore(schedDB.getEndTime().minusMinutes(30))){
            boolean bookedAppoint = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(docDB, date, startTime);

            if(!bookedAppoint){
                availableSlots.add(startTime);
            }
            startTime = startTime.plusMinutes(30);
        }
        return availableSlots;
    }

    @Override
    public AppointmentDto bookAppointment(AppointmentDto appointmentDto, User user) {
        if(!user.getRole().equals("PATIENT")){
            if(user.getRole().equals("ADMIN") || user.getRole().equals("DOCTOR")){
                throw new RuntimeException("Access denied");
            }
        }

        Patient patientDB = patientRepository.findByUser(user);
        if(patientDB == null){
            patientDB = mapToPatient(user);
            patientRepository.save(patientDB);
            user.setRole(Role.PATIENT);
            userRepository.save(user);
        }

        Doctor docDB = doctorRepository.findById(appointmentDto.getDoctorId()).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        boolean booked = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(
                docDB, appointmentDto.getAppointmentDate(), appointmentDto.getAppointmentTime());

        if(booked){
            throw new RuntimeException("This slot has been booked");
        }

        Appointment appointment = mapToAppointment(appointmentDto, docDB, patientDB);
        Appointment saved = appointmentRepository.save(appointment);

        return mapToAppointmentDto(saved);
    }



    private Patient mapToPatient(User user) {
        return Patient.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .address(user.getAddress())
                .user(user)
                .password(user.getPassword())
                .dob(user.getDob())
                .email(user.getEmail())
                .role(Role.PATIENT)
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    private Appointment mapToAppointment(AppointmentDto appointmentDto, Doctor docDB, Patient patientDB) {
        return Appointment.builder()
                .appointmentDate(appointmentDto.getAppointmentDate())
                .appointmentTime(appointmentDto.getAppointmentTime())
                .doctor(docDB)
                .patient(patientDB)
                .note(appointmentDto.getNote())
                .status(Status.APPROVED)
                .build();
    }

    private AppointmentDto mapToAppointmentDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setNote(appointment.getNote());
        return dto;
    }


}
