package com.SecurityLockers.SecureDeliveryLockers.modules.support.dto;

import lombok.Data;

@Data
public class ComplaintRequest {
    private String type;
    private String subject;
    private String description;
    private String priority;
}
