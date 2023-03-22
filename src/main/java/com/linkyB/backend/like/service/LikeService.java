package com.linkyB.backend.like.service;

import com.linkyB.backend.like.converter.LikeConverter;
import com.linkyB.backend.like.dto.LikeDto;
import com.linkyB.backend.like.entity.UserLikes;
import com.linkyB.backend.like.mapper.LikeMapper;
import com.linkyB.backend.like.repository.LikeRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final LikeConverter likeConverter;

    // 유저 좋아요 등록
    @Transactional
    public LikeDto userLikes(long userGetLikes, long userId) {
        User Give = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User Get = userRepository.findById(userGetLikes).orElseThrow(UserNotFoundException::new);
        Get.increaseLikeCount();

        UserLikes entity = likeRepository.save(likeConverter.userLikes(Give, Get));
        LikeDto dto = LikeMapper.INSTANCE.entityToDto(entity);

        return dto;
    }
}
