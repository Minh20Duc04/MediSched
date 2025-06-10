    package com.CareBook.MediSched.Controller;

    import com.CareBook.MediSched.Dto.AppointmentDto;
    import com.CareBook.MediSched.Model.User;
    import com.CareBook.MediSched.Service.AppointmentService;
    import lombok.Getter;
    import lombok.RequiredArgsConstructor;
    import org.springframework.format.annotation.DateTimeFormat;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.util.List;

    @RestController
    @RequestMapping("/appointment")
    @RequiredArgsConstructor
    public class AppointmentController {

        private final AppointmentService appointmentService;

        @GetMapping("/available-slots")
        public ResponseEntity<List<LocalTime>> getAvailableSlots(@RequestParam Long doctorId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            return ResponseEntity.ok(appointmentService.getAvailableSlots(doctorId, date));
        }

        @PostMapping("/book") //cho patient, user
        public ResponseEntity<AppointmentDto> bookAppointment(@RequestBody AppointmentDto appointmentDto, Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(appointmentService.bookAppointment(appointmentDto, user));
        }

        @PutMapping("/update/{appointmentId}")
        public ResponseEntity<String> updateAppointment(@PathVariable Long appointmentId, Authentication authentication, @RequestParam String status){
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(appointmentService.updateAppointment(appointmentId, user, status));
        }

        @GetMapping("/getBy-doctor")
        public ResponseEntity<List<AppointmentDto>> getAllByDoc(Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(appointmentService.getAllByDoc(user));
        }

        @GetMapping("/me")
        public ResponseEntity<List<AppointmentDto>> getMyAppointments(Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(appointmentService.getAppointmentsByUser(user));
        }


    }
