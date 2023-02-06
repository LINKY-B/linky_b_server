package com.linkyB.backend.user.repository;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.presentation.dto.UserInterestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInterestRepository extends JpaRepository<Interest, Long> {
}