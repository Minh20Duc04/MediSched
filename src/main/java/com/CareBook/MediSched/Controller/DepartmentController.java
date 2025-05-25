package com.CareBook.MediSched.Controller;

import com.CareBook.MediSched.Model.Department;
import com.CareBook.MediSched.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/department")

public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/createDepartment")
    public ResponseEntity<Department> createDepartment(@RequestParam String name, @RequestParam String description){
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(name, description));
    }


}
