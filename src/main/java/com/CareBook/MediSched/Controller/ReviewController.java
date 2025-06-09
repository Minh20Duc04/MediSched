package com.CareBook.MediSched.Controller;

import com.CareBook.MediSched.Dto.ReviewDto;
import com.CareBook.MediSched.Model.User;
import com.CareBook.MediSched.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/evaluate")
    public ResponseEntity<String> evaluateDoc(@RequestBody ReviewDto reviewDto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(reviewService.evaluateDoc(user, reviewDto));
    }

    @GetMapping("/get-all/{doctorId}")
    public ResponseEntity<List<ReviewDto>> getAllById(@PathVariable Long doctorId){
        return ResponseEntity.ok(reviewService.getAllById(doctorId));
    }


}
