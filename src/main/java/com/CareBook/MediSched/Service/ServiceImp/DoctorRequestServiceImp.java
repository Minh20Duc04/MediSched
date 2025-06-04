package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.*;
import com.CareBook.MediSched.Repository.DepartmentRepository;
import com.CareBook.MediSched.Repository.DoctorRequestRepository;
import com.CareBook.MediSched.Service.DoctorRequestService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorRequestServiceImp implements DoctorRequestService {

    private final DoctorRequestRepository doctorRequestRepository;
    private final DepartmentRepository departmentRepository;
    private final Cloudinary cloudinary;


    @Override
    public DoctorRequestDto createDoctorRequest(DoctorRequestDto doctorRequestDto, User user, MultipartFile file) {
        validateRequest(doctorRequestDto);

        System.out.println("Specialty received: '" + doctorRequestDto.getSpecialty() + "'");
        String sp = doctorRequestDto.getSpecialty().trim().toUpperCase();
        Specialty specialty = Specialty.valueOf(sp);

        Department department = departmentRepository.findById(doctorRequestDto.getDepartmentId()).orElseThrow(()-> new IllegalArgumentException("This Department not exist"));

        Set<DayOfWeek> days = doctorRequestDto.getDaysOfWeek()
                .stream()
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());

        String imageUrl = uploadFile(file);

        DoctorRequest doctorRequest =  DoctorRequest.builder()
                .startTime(doctorRequestDto.getStartTime())
                .endTime(doctorRequestDto.getEndTime())
                .status(Status.PENDING)
                .user(user)
                .specialty(specialty)
                .department(department)
                .daysOfWeek(days)
                .fee(doctorRequestDto.getFee())
                .description(doctorRequestDto.getDescription())
                .imageUrl(imageUrl)
                .build();

        DoctorRequest savedDocRequest = doctorRequestRepository.save(doctorRequest);

        doctorRequestDto.setStatus(savedDocRequest.getStatus().name());
        doctorRequestDto.setId(savedDocRequest.getId());
        return doctorRequestDto;
    }

    @Override
    public List<DoctorRequestDto> getAllDoctorRequests() {
        List<DoctorRequest> doctorRequests = doctorRequestRepository.findAll();
        List<DoctorRequestDto> doctorRequestDtos = doctorRequests.stream().map(this::docRequestToDto).collect(Collectors.toList());
        return doctorRequestDtos;
    }


    public void validateRequest(DoctorRequestDto doctorRequestDto){
        String sp = doctorRequestDto.getSpecialty().trim().toUpperCase();
        try {
            Specialty.valueOf(sp);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid specialty: " + doctorRequestDto.getSpecialty());
        }

        for(String day : doctorRequestDto.getDaysOfWeek()){
            try {
                DayOfWeek.valueOf(day.toUpperCase());
            }catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Invalid day of week: " + day);
            }
        }

        if(doctorRequestDto.getStartTime() == null || doctorRequestDto.getEndTime() == null){
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }

        if(doctorRequestDto.getStartTime().isAfter(doctorRequestDto.getEndTime())){
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if(doctorRequestDto.getFee().equals(null)  || doctorRequestDto.getFee() <= 100000){
            throw new IllegalArgumentException("Invalid fee");
        }
    }

    private DoctorRequestDto docRequestToDto(DoctorRequest doctorRequest){
        return new DoctorRequestDto(doctorRequest.getId(), doctorRequest.getStatus().name(), doctorRequest.getSpecialty().name(), doctorRequest.getDaysOfWeek().stream().map(DayOfWeek::name).collect(Collectors.toList()), doctorRequest.getDepartment().getId(), doctorRequest.getStartTime(), doctorRequest.getEndTime(), doctorRequest.getFee(), doctorRequest.getDescription());
    }

    private String uploadFile(MultipartFile file){
        try{
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("secure_url").toString();
        }catch (IOException e){
            throw new RuntimeException("Lỗi khi upload file: " + e.getMessage());
        }
    }

}
