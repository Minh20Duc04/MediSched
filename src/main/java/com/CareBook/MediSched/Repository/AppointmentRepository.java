package com.CareBook.MediSched.Repository;

import com.CareBook.MediSched.Model.Appointment;
import com.CareBook.MediSched.Model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(Doctor docDB, LocalDate date, LocalTime startTime);

    List<Appointment> findAllByDoctor(Doctor docDB);
}
