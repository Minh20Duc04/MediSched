package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.AppointmentDto;
import com.CareBook.MediSched.Model.*;
import com.CareBook.MediSched.Repository.*;
import com.CareBook.MediSched.Service.AppointmentService;
import jakarta.transaction.Transactional;
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

        PaymentMethod paymentMethod = PaymentMethod.valueOf(appointmentDto.getPaymentMethod().name().toUpperCase());

        Appointment appointment = mapToAppointment(appointmentDto, docDB, patientDB, paymentMethod);
        Appointment saved = appointmentRepository.save(appointment);

        String VNPayUrl = createVnPayUrl(saved);

        sendEmail(patientDB.getEmail(), appointment, VNPayUrl);

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

    @Override
    public String updateAppointment(Long appointmentId, User user, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(()-> new IllegalArgumentException("Appointment not found"));

        if(!user.getDoctor().getId().equals(appointment.getDoctor().getId())){
            throw new RuntimeException("You don't have permission to update");
        }

        Status newStatus;
        try{
            newStatus = Status.valueOf(status.trim().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new RuntimeException("Invalid status" + status);
        }

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);

        return "Appointment status updated to " + newStatus;
    }

    @Override
    @Transactional
    public List<AppointmentDto> getAppointmentsByUser(User user) {
        Patient patientDB = patientRepository.findByUserWithAppointments(user).orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Appointment> appointments = patientDB.getAppointments();
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

    private Appointment mapToAppointment(AppointmentDto appointmentDto, Doctor docDB, Patient patientDB, PaymentMethod paymentMethod) {
        return Appointment.builder()
                .appointmentDate(appointmentDto.getAppointmentDate())
                .appointmentTime(appointmentDto.getAppointmentTime())
                .doctor(docDB)
                .patient(patientDB)
                .note(appointmentDto.getNote())
                .status(Status.APPROVED)
                .paymentMethod(paymentMethod)
                .build();
    }

    private AppointmentDto mapToAppointmentDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setNote(appointment.getNote());
        dto.setPaymentMethod(appointment.getPaymentMethod());
        return dto;
    }

    public void sendEmail(String email, Appointment appointment, String paymentUrl) {
        String doctorName = appointment.getDoctor().getUser().getFirstName() + " " +
                appointment.getDoctor().getUser().getLastName();
        String date = appointment.getAppointmentDate().toString();
        String time = appointment.getAppointmentTime().toString();

        StringBuilder content = new StringBuilder();
        content.append("Dear Patient,\n\n")
                .append("You have booked an appointment with:\n")
                .append("Doctor: Dr. ").append(doctorName).append("\n")
                .append("Date: ").append(date).append("\n")
                .append("Time: ").append(time).append("\n\n");

        if(appointment.getPaymentMethod() == PaymentMethod.ONLINE){
            content.append("To complete your booking, please pay online via the link below:\n")
                    .append(paymentUrl).append("\n\n");
        } else{
            content.append("Please pay at the clinic on the day of your appointment.\n\n");
        }

        content.append("Thank you for choosing MediSched.\nBest regards,\nMediSched - CareBook");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Appointment Confirmation - MediSched");
        message.setText(content.toString());
        message.setFrom(fromEmail);

        mailSender.send(message);
    }


    public String createVnPayUrl(Appointment appointment) {
        // Đường dẫn VNPay
        String baseUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:8080/api/payment/vnpay-return";

        return baseUrl
                + "?vnp_Amount=1000000"
                + "&vnp_TxnRef=" + appointment.getId()
                + "&vnp_OrderInfo=Thanh+toan+lich+hen"
                + "&vnp_ReturnUrl=" + returnUrl;
    }




}
