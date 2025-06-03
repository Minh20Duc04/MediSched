package com.CareBook.MediSched.Repository;

import com.CareBook.MediSched.Model.Patient;
import com.CareBook.MediSched.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUser(User user);
}
