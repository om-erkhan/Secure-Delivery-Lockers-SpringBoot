package com.SecurityLockers.SecureDeliveryLockers.modules.auth.repository;

import com.SecurityLockers.SecureDeliveryLockers.modules.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
