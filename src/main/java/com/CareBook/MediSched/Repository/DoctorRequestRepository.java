package com.CareBook.MediSched.Repository;

import com.CareBook.MediSched.Model.DoctorRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRequestRepository extends JpaRepository<DoctorRequest, Long> {

}
