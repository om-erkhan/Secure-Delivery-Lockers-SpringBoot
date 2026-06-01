package com.SecurityLockers.SecureDeliveryLockers.modules.support.service;

import com.SecurityLockers.SecureDeliveryLockers.modules.auth.model.User;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.dto.ComplaintRequest;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.dto.FeedbackRequest;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.model.Complaint;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.model.Feedback;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.repository.ComplaintRepository;
import com.SecurityLockers.SecureDeliveryLockers.modules.support.repository.FeedbackRepository;
import com.SecurityLockers.SecureDeliveryLockers.utility.AuthUtils;
import com.SecurityLockers.SecureDeliveryLockers.utility.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SupportService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private EmailService emailService;

    public Complaint submitComplaint(ComplaintRequest request) throws Exception {
        User currentUser = authUtils.getCurrentUser();
        
        Complaint complaint = Complaint.builder()
                .user(currentUser)
                .type(request.getType() != null ? request.getType() : "Complaint")
                .subject(request.getSubject())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : "Normal")
                .status("Open")
                .build();
                
        Complaint saved = complaintRepository.save(complaint);

        // Send email to user
        String emailText = "Dear User,\n\n" +
                "We have successfully received your " + complaint.getType().toLowerCase() + " regarding '" + complaint.getSubject() + "'.\n" +
                "Our team will look into it and get back to you within 24 hours.\n\n" +
                "Thank you,\n" +
                "Secure Delivery Lockers Support";
                
        emailService.sendMail(currentUser.getEmail(), "Your " + complaint.getType() + " has been received", emailText, "");

        return saved;
    }

    public Feedback submitFeedback(FeedbackRequest request) throws Exception {
        User currentUser = authUtils.getCurrentUser();

        Feedback feedback = Feedback.builder()
                .user(currentUser)
                .rating(request.getRating())
                .feedbackText(request.getFeedbackText())
                .build();

        return feedbackRepository.save(feedback);
    }

    public List<Complaint> getComplaints() throws Exception {
        User currentUser = authUtils.getCurrentUser();
        return complaintRepository.findByUserOrderByCreatedAtDesc(currentUser);
    }

    public List<Feedback> getFeedbacks() throws Exception {
        User currentUser = authUtils.getCurrentUser();
        return feedbackRepository.findByUserOrderByCreatedAtDesc(currentUser);
    }
}
