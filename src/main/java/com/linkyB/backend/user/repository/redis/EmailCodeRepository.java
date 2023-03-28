package com.linkyB.backend.user.repository.redis;

import com.linkyB.backend.user.domain.redis.EmailCode;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EmailCodeRepository extends CrudRepository<EmailCode, Long> {
    Optional<EmailCode> findByEmailAndUserNickName(String email, String userNickName);

    List<EmailCode> findAllByEmailAndUserNickName(String email, String userNickName);
}
