package com.CareBook.MediSched.Repository;

import com.CareBook.MediSched.Model.Patient;
import com.CareBook.MediSched.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUser(User user);

    @Query("SELECT p FROM Patient p JOIN FETCH p.appointments WHERE p.user = :user")
    Optional<Patient> findByUserWithAppointments(@Param("user") User user);
}
