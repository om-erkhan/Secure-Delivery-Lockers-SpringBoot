package com.SecurityLockers.SecureDeliveryLockers.modules.auth.service;

import com.SecurityLockers.SecureDeliveryLockers.modules.auth.dto.RegisterRequestDTO;
import com.SecurityLockers.SecureDeliveryLockers.modules.auth.model.User;
import com.SecurityLockers.SecureDeliveryLockers.modules.auth.repository.AuthRepository;
import com.SecurityLockers.SecureDeliveryLockers.modules.lockers.dto.AuthResponse;
import com.SecurityLockers.SecureDeliveryLockers.messaging.producer.EmailProducer;
import com.SecurityLockers.SecureDeliveryLockers.utility.AuthEnums;
import com.SecurityLockers.SecureDeliveryLockers.utility.JwtUtil;
import com.SecurityLockers.SecureDeliveryLockers.utility.Util;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private EmailProducer emailProducer;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public AuthResponse login(RegisterRequestDTO dto) {
        Optional<User> optionalUser = authRepository.findByEmail(dto.getEmail());
        int otp = Util.generateUniqueOtp();
//       for user login
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                return new AuthResponse(null, "Wrong Password", AuthEnums.WRONG_PASSWORD);
            }
            if (!user.getIsVerified()) {
                String otpKey = "OTP:USER:" + user.getEmail();
                redisTemplate.opsForValue().set(otpKey, String.valueOf(otp), Duration.ofMinutes(5));
                emailProducer.sendOtpEmail(user.getEmail(), String.valueOf(otp));
                
                // Update user OTP to avoid unique constraint violations
                user.setOtp(otp);
                authRepository.save(user);
                
                return new AuthResponse(null, "User exists but not verified. Please verify OTP. (Test OTP: " + otp + ")", AuthEnums.OTP_REQUIRED);
            }
            if (user.getIsProfileCompleted() == false) {
                String token = jwtUtil.generateToken(user.getEmail());
                String sessionKey = "SESSION:" + token;
                redisTemplate.opsForValue().set(sessionKey, user.getEmail(), Duration.ofHours(2));
                return new AuthResponse(token, "Complete your profile to continue.", AuthEnums.PROFILE_INCOMPLETE);
            } else {
                String token = jwtUtil.generateToken(user.getEmail());
                String sessionKey = "SESSION:" + token;
                redisTemplate.opsForValue().set(sessionKey, user.getEmail(), Duration.ofHours(2));
                return new AuthResponse(token, "Login successful", AuthEnums.LOGIN_SUCCESS);
            }
        } else {
//        for signup
            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            User newUser = User.builder()
                    .email(dto.getEmail())
                    .password(hashedPassword)
                    .isVerified(false)
                    .otp(otp) // Set generated OTP to avoid constraint violation
                    .build();
            authRepository.save(newUser);

            String otpKey = "OTP:USER:" + newUser.getEmail();
            redisTemplate.opsForValue().set(otpKey, String.valueOf(otp), Duration.ofMinutes(5));
            emailProducer.sendOtpEmail(dto.getEmail(), String.valueOf(otp));
            return new AuthResponse(null, "Otp sent to your email. Please verify. (Test OTP: " + otp + ")", AuthEnums.OTP_SENT);

        }
    }


    @Transactional
    public Map<String, Object> verifyOtp(String email, String otp) {
        String otpKey = "OTP:USER:" + email;
        String cachedOtp = redisTemplate.opsForValue().get(otpKey);

        if (cachedOtp == null) {
            throw new RuntimeException("OTP expired or not found");
        }

        if (!cachedOtp.equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        redisTemplate.delete(otpKey);

        User user = authRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getIsVerified() != null && user.getIsVerified()) {
            // If they are trying to verify but already verified, maybe they are just logging in from a new device?
            // Actually the login logic handles verified users differently.
            // But let's just proceed to allow them to get a token.
        }

        user.setIsVerified(true);
        authRepository.save(user);


        String token = jwtUtil.generateToken(user.getEmail());
        String sessionKey = "SESSION:" + token;
        redisTemplate.opsForValue().set(sessionKey, user.getEmail(), Duration.ofHours(2));

        return Map.of(
            "token", token,
            "isProfileCompleted", user.getIsProfileCompleted() != null && user.getIsProfileCompleted()
        );
    }

    public void logout(String token) {
        String sessionKey = "SESSION:" + token;
        redisTemplate.delete(sessionKey);
    }

    public boolean isSessionValid(String token) {
        String sessionKey = "SESSION:" + token;
        return redisTemplate.hasKey(sessionKey);
    }
}
