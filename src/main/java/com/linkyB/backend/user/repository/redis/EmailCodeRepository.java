package com.linkyB.backend.user.repository.redis;

import com.linkyB.backend.user.domain.redis.EmailCode;
import com.linkyB.backend.user.domain.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailCodeRepository extends CrudRepository<EmailCode, Long> {
    Optional<EmailCode> findByEmailAndUserName(String email, String username);
}
