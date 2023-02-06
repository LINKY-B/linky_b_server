package com.linkyB.backend.user.repository;

import com.linkyB.backend.user.domain.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPersonalityRepository extends JpaRepository<Personality, Long> {

}
