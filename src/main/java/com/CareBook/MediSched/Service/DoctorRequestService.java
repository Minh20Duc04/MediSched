package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Dto.DoctorRequestDto;
import com.CareBook.MediSched.Model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DoctorRequestService {
    DoctorRequestDto createDoctorRequest(DoctorRequestDto doctorRequestDto, User user, MultipartFile file);

    List<DoctorRequestDto> getAllDoctorRequests();


}
