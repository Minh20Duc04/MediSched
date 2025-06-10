package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Model.Department;
import com.CareBook.MediSched.Repository.DepartmentRepository;
import com.CareBook.MediSched.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImp implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department createDepartment(String name, String description) {
        if(departmentRepository.existsByNameAndDescription(name,description)){
            throw new IllegalArgumentException("Department with this information already exists");
        }

        Department newDepartment = Department.builder()
                .name(name)
                .description(description)
                .build();

        return departmentRepository.save(newDepartment);
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }
}
