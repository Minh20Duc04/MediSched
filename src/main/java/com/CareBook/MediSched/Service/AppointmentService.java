package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Dto.AppointmentDto;
import com.CareBook.MediSched.Model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {
    List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date);

    AppointmentDto bookAppointment(AppointmentDto appointmentDto, User user);


    List<AppointmentDto> getAllByDoc(User user);

    String updateAppointment(Long appointmentId, User user, String status);

    List<AppointmentDto> getAppointmentsByUser(User user);
}
