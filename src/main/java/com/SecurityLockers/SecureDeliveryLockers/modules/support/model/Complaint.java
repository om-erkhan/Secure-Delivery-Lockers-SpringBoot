package com.SecurityLockers.SecureDeliveryLockers.modules.support.model;

import com.SecurityLockers.SecureDeliveryLockers.modules.auth.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "complaints")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password", "otp", "userProfile"})
    private User user;

    @Column(nullable = false)
    private String type; // Complaint, Query, Feedback

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String priority; // Low, Normal, High

    @Column(nullable = false)
    private String status; // Open, Resolved, Closed

    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = "Open";
        }
    }
}
