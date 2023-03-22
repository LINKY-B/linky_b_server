package com.linkyB.backend.home.service;

import com.linkyB.backend.home.repository.PagingRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.mapper.UserMapper;
import com.linkyB.backend.user.dto.UserListDto;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final PagingRepository pagingRepository;
    private final UserRepository userRepository;

    // 졸업생 유저 리스트 조회
    public List<UserListDto> TrueList(int offset, int limit, long userId) {
        List<User> users = pagingRepository.findAllByGradStatusTrue(offset, limit,userId);
        List<UserListDto> dto = UserMapper.INSTANCE.entityToDtoList(users);
        return dto;
    }

    // 재학생 유저 리스트 조회
    public List<UserListDto> FalseList(int offset, int limit, long userId) {
        List<User> users = pagingRepository.findAllByGradStatusFalse(offset, limit, userId);
        List<UserListDto> dto = UserMapper.INSTANCE.entityToDtoList(users);

        return dto;
    }

    // 닉네임 검색 기능
    @Transactional
    public List<UserListDto> search(String nickName){
        List<User> userList = userRepository.findByuserNickNameContaining(nickName);
        List<UserListDto> dto = UserMapper.INSTANCE.entityToDtoList(userList);

        return dto;
    }

}
