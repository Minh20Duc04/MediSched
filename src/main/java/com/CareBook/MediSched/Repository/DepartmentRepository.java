package com.CareBook.MediSched.Repository;

import com.CareBook.MediSched.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByNameAndDescription(String name, String description);


}
