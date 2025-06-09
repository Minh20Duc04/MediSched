package com.CareBook.MediSched.Repository;

import com.CareBook.MediSched.Model.Doctor;
import com.CareBook.MediSched.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {


    boolean existsByUserId(Long id);

    @Query("SELECT d FROM Doctor d " +
            "WHERE (:name IS NULL OR LOWER(d.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:specialty IS NULL OR LOWER(d.specialty) LIKE LOWER(CONCAT('%', :specialty, '%')))")
    Page<Doctor> searchDoctors(@Param(value = "name") String name,@Param(value = "specialty") String specialty, Pageable pageable);

    Optional<Doctor> findByUser(User user);
}
