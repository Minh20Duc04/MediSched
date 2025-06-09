package com.CareBook.MediSched.Service;

import com.CareBook.MediSched.Dto.ReviewDto;
import com.CareBook.MediSched.Model.User;

import java.util.List;

public interface ReviewService {
    String evaluateDoc(User user, ReviewDto reviewDto);

    List<ReviewDto> getAllById(Long doctorId);
}
