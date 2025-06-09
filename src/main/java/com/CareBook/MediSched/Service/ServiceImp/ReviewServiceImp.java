package com.CareBook.MediSched.Service.ServiceImp;

import com.CareBook.MediSched.Dto.ReviewDto;
import com.CareBook.MediSched.Model.*;
import com.CareBook.MediSched.Repository.DoctorRepository;
import com.CareBook.MediSched.Repository.PatientRepository;
import com.CareBook.MediSched.Repository.ReviewRepository;
import com.CareBook.MediSched.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public String evaluateDoc(User user, ReviewDto reviewDto) {
        if(!user.getRole().equals(Role.PATIENT)){
            throw new IllegalArgumentException("Only patients can submit reviews");
        }

        Doctor docDB = doctorRepository.findById(reviewDto.getDoctorId()).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        Patient patientDB = user.getPatient();

        Review review = mapToReview(reviewDto, patientDB, docDB);
        reviewRepository.save(review);

        return "Review submitted successfully";
    }

    private Review mapToReview(ReviewDto reviewDto, Patient patientDB, Doctor docDB) {
        return Review.builder()
                .comment(reviewDto.getComment())
                .rating(reviewDto.getRating())
                .patient(patientDB)
                .doctor(docDB)
                .build();
    }

    @Override
    public List<ReviewDto> getAllById(Long doctorId) {
        Doctor docDB = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        List<Review> reviews = docDB.getReviews();

        return reviews.stream()
                .map(this::mapToReviewDto)
                .collect(Collectors.toList());
    }

    private ReviewDto mapToReviewDto(Review review) {
        return new ReviewDto(
                review.getComment(),
                review.getRating(),
                review.getDoctor().getId()
        );
    }
}

