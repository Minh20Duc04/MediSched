package com.CareBook.MediSched.Repository;

import com.CareBook.MediSched.Model.Department;
import com.CareBook.MediSched.Model.Doctor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByNameAndDescription(String name, String description);

}
