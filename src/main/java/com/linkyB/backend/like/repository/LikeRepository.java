package com.linkyB.backend.like.repository;


import com.linkyB.backend.like.entity.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<UserLikes, Long> {

}
