package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Model.Department;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(String name, String description);

    List<Department> getAll();
}
