package com.SecurityLockers.SecureDeliveryLockers.messaging.consumer;

import com.SecurityLockers.SecureDeliveryLockers.config.RabbitMQConfig;
import com.SecurityLockers.SecureDeliveryLockers.messaging.dto.EmailMessage;
import com.SecurityLockers.SecureDeliveryLockers.utility.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consumeEmail(EmailMessage emailMessage) {
        try {
            log.info("Consuming email message for: {}", emailMessage.getTo());
            
            // Print a prominent log containing the email details so they are visible in Render logs
            log.info("=================================================");
            log.info("📬 DEVELOPER EMAIL LOG - FALLBACK DISPLAY");
            log.info("To: {}", emailMessage.getTo());
            log.info("Subject: {}", emailMessage.getSubject());
            log.info("Content: {}", emailMessage.getBody());
            if (emailMessage.getUserOtp() != null) {
                log.info("User OTP: {}", emailMessage.getUserOtp());
            }
            if (emailMessage.getDeliveryOtp() != null) {
                log.info("Delivery OTP: {}", emailMessage.getDeliveryOtp());
            }
            log.info("=================================================");

            // Handle different email types
            switch (emailMessage.getEmailType()) {
                case OTP_REGISTRATION:
                case OTP_LOGIN:
                    emailService.sendMail(emailMessage.getTo(), emailMessage.getBody().split(":")[1].trim());
                    break;
                    
                case RESERVATION_OTP:
                    emailService.sendMail(
                            emailMessage.getTo(),
                            emailMessage.getUserOtp(),
                            emailMessage.getDeliveryOtp()
                    );
                    break;
                    
                case PARCEL_DELIVERED:
                case PARCEL_PICKED_UP:
                case RESERVATION_EXPIRED:
                case RESERVATION_EXPIRING_SOON:
                case RENEW_OTP_REQUIRED:
                case REMINDER:
                    emailService.sendMail(
                            emailMessage.getTo(),
                            emailMessage.getSubject(),
                            emailMessage.getBody(),
                            ""
                    );
                    break;
                    
                default:
                    // Generic email sending
                    emailService.sendMail(
                            emailMessage.getTo(),
                            emailMessage.getSubject(),
                            emailMessage.getBody(),
                            ""
                    );
            }
            
            log.info("Email sent successfully to: {}", emailMessage.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to {} (likely due to Render SMTP port blocks): {}", emailMessage.getTo(), e.getMessage());
            log.warn("Swallowing connection exception to prevent RabbitMQ infinite retry loops.");
        }
    }
}

