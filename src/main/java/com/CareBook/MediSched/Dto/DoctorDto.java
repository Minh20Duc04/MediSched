package com.CareBook.MediSched.Dto;


import com.CareBook.MediSched.Model.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DoctorDto {
    private Long id;

    private String fullName;

    private String specialty;

    private Department department;

    private String email;

    private String imageUrl;

    private Double fee;

    private String description;

    private String role;

}
