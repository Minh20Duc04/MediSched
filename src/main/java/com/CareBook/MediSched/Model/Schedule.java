package com.CareBook.MediSched.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private LocalTime startTime;

    private LocalTime endTime;

    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(name = "schedule_days", joinColumns = @JoinColumn(name = "schedule_id"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "day_of_week")
    private Set<DayOfWeek> daysOfWeek;


}
