package com.CareBook.MediSched.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DoctorRequestDto {

    private Long id;

    private String status;

    private String specialty;

    private List<String> daysOfWeek;

    private Long departmentId;

    private LocalTime startTime;

    private LocalTime endTime;

}
