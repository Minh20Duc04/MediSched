package com.CareBook.MediSched.Controller;

import com.CareBook.MediSched.Model.Department;
import com.CareBook.MediSched.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/department")

public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/createDepartment")
    public ResponseEntity<Department> createDepartment(@RequestParam String name, @RequestParam String description){
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(name, description));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok(departmentService.getAll());
    }

}
