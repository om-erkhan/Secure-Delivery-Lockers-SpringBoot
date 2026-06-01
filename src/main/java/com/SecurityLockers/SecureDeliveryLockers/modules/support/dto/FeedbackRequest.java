package com.SecurityLockers.SecureDeliveryLockers.modules.support.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private int rating;
    private String feedbackText;
}
