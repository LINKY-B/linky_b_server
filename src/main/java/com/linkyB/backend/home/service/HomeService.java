package com.linkyB.backend.home.service;

import com.linkyB.backend.home.repository.PagingRepository;
import com.linkyB.backend.match.entity.status;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.dto.UserListResponseDto;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final PagingRepository pagingRepository;
    private final UserRepository userRepository;

    // 유저 리스트 조회
    public List<UserListResponseDto> getUserLists(int offset, int limit, long userId, boolean isGraduate) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        final String schoolName = user.getUserSchoolName();

        return pagingRepository.findAll(offset, limit, userId, schoolName, isGraduate)
                .stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 닉네임 검색 기능
    @Transactional
    public List<UserListResponseDto> search(String nickName){
        return userRepository.findByuserNickNameContaining(nickName)
                .stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());
    }

}
