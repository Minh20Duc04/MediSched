package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Model.Department;

public interface DepartmentService {
    Department createDepartment(String name, String description);
}
