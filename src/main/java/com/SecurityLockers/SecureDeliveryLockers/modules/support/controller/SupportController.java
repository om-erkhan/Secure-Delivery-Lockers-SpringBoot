package com.SecurityLockers.SecureDeliveryLockers.modules.support.controller;

import com.SecurityLockers.SecureDeliveryLockers.modules.support.dto.ComplaintRequest;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.dto.FeedbackRequest;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.model.Complaint;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.model.Feedback;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.service.SupportService;
import com.SecurityLockers.SecureDeliveryLockers.utility.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @PostMapping("/submit-complaint")
    public ResponseEntity<?> submitComplaint(@RequestBody ComplaintRequest request) {
        try {
            Complaint complaint = supportService.submitComplaint(request);
            return ResponseBuilder.success(complaint, "Complaint submitted successfully!");
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/submit-feedback")
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackRequest request) {
        try {
            Feedback feedback = supportService.submitFeedback(request);
            return ResponseBuilder.success(feedback, "Feedback submitted successfully!");
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/get-complaints")
    public ResponseEntity<?> getComplaints() {
        try {
            List<Complaint> complaints = supportService.getComplaints();
            return ResponseBuilder.success(complaints, "Complaints fetched successfully!");
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/get-feedbacks")
    public ResponseEntity<?> getFeedbacks() {
        try {
            List<Feedback> feedbacks = supportService.getFeedbacks();
            return ResponseBuilder.success(feedbacks, "Feedbacks fetched successfully!");
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
