package com.linkyB.backend.home.service;

import com.linkyB.backend.home.repository.PagingRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.mapper.UserMapper;
import com.linkyB.backend.user.presentation.dto.UserListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final PagingRepository pagingRepository;

    // 재학생 유저 리스트 조회
    public List<UserListDto> TrueList(int offset, int limit, long userId) {
        List<User> users = pagingRepository.findAllByGradStatusTrue(offset, limit,userId);
        List<UserListDto> dto = UserMapper.INSTANCE.entityToDtoList(users);
        return dto;
    }

    // 졸업생 유저 리스트 조회
    public List<UserListDto> FalseList(int offset, int limit, long userId) {
        List<User> users = pagingRepository.findAllByGradStatusFalse(offset, limit, userId);
        List<UserListDto> dto = UserMapper.INSTANCE.entityToDtoList(users);

        return dto;
    }
}
