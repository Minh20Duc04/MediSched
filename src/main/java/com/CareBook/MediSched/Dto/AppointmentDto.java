package com.CareBook.MediSched.Dto;

import com.CareBook.MediSched.Model.Doctor;
import com.CareBook.MediSched.Model.Patient;
import com.CareBook.MediSched.Model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private Long patientId;

    private Long doctorId;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String note;

    @NotNull
    private PaymentMethod paymentMethod;
}
