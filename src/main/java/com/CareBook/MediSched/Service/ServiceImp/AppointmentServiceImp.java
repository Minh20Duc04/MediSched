package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.AppointmentDto;
import com.CareBook.MediSched.Model.*;
import com.CareBook.MediSched.Repository.*;
import com.CareBook.MediSched.Service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImp implements AppointmentService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

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

        sendEmail(patientDB.getEmail(),saved);

        return mapToAppointmentDto(saved);
    }

    @Override
    public List<AppointmentDto> getAllByDoc(User user) {
        if(!user.getRole().equals(Role.DOCTOR)){
            throw new RuntimeException("You are not a doctor");
        }

        Doctor docDB = doctorRepository.findByUser(user).orElseThrow(()-> new IllegalArgumentException("Doctor not found"));
        List<Appointment> appointments = appointmentRepository.findAllByDoctor(docDB);

        return appointments.stream().map(this::mapToAppointmentDto).collect(Collectors.toList());
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

    public void sendEmail(String email, Appointment appointment) {
        try {
            String doctorName = appointment.getDoctor().getUser().getFirstName() + " " +
                    appointment.getDoctor().getUser().getLastName();
            String date = appointment.getAppointmentDate().toString();
            String time = appointment.getAppointmentTime().toString();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Appointment Confirmation - MediSched");
            message.setText("Dear Patient,\n\n"
                    + "Your appointment has been successfully booked with the following details:\n\n"
                    + "Doctor: Dr. " + doctorName + "\n"
                    + "Date: " + date + "\n"
                    + "Time: " + time + "\n\n"
                    + "Please make sure to arrive at least 10 minutes before your scheduled time.\n"
                    + "If you have any questions, feel free to contact our support team.\n\n"
                    + "Thank you for choosing MediSched.\n"
                    + "Best regards,\n"
                    + "MediSched - CareBook");
            message.setFrom(fromEmail);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }




}
