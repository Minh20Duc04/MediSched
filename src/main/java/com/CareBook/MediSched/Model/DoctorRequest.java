package com.CareBook.MediSched.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "doctor_request")

public class DoctorRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;

    private LocalTime endTime;

    @Enumerated(value = EnumType.STRING)
    private Specialty specialty;

    private String imageUrl;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(value = EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek;

    @ManyToOne
    private Department department;

    @OneToOne
    private User user;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Double fee;

    private String description;

}
