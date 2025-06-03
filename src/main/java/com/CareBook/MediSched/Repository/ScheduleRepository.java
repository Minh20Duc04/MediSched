package com.CareBook.MediSched.Repository;

import com.CareBook.MediSched.Model.Doctor;
import com.CareBook.MediSched.Model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {


    Schedule findByDoctorAndDaysOfWeekContaining(Doctor docDB, DayOfWeek dayOfWeek);
}
