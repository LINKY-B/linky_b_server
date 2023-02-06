package com.linkyB.backend.user.repository;

import com.linkyB.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserPhone(String userPhone);
    boolean existsByUserPhone(String userPhone);
}
