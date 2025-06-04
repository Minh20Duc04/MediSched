package com.CareBook.MediSched.Dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDto {

    private Long id;

    private String status;

    private String specialty;

    private List<String> daysOfWeek;

    private Long departmentId;

    private LocalTime startTime;

    private LocalTime endTime;

    private Double fee;

    private String description;


}
