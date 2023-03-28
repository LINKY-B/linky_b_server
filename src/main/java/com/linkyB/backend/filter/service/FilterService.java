package com.linkyB.backend.filter.service;

import com.linkyB.backend.filter.dto.UserFilterResponseDto;
import com.linkyB.backend.filter.converter.FilterConverter;
import com.linkyB.backend.filter.dto.PostFilterReq;
import com.linkyB.backend.filter.entity.GenderForFilter;
import com.linkyB.backend.filter.entity.GradeForFilter;
import com.linkyB.backend.filter.entity.MajorForFilter;
import com.linkyB.backend.filter.entity.MbtiForFilter;
import com.linkyB.backend.filter.repository.GenderForFilterRepository;
import com.linkyB.backend.filter.repository.GradeForFilterRepository;
import com.linkyB.backend.filter.repository.MajorForFilterRepository;
import com.linkyB.backend.filter.repository.MbtiForFilterRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.dto.UserListResponseDto;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FilterService {

    private final UserRepository userRepository;
    private final GradeForFilterRepository gradeForFilterRepository;
    private final MbtiForFilterRepository mbtiForFilterRepository;
    private final MajorForFilterRepository majorForFilterRepository;
    private final GenderForFilterRepository genderForFilterRepository;
    private final FilterConverter filterConverter;

    // 필터 저장 후 필터 적용된 졸업생 리스트 반환
    @Transactional
    public List<UserListResponseDto> TrueList(long userId, PostFilterReq dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다. "));

        // 기존에 설정한 필터가 있다면 삭제 후 새로운 필터 적용
        if (!genderForFilterRepository.findAllByUser(userId).isEmpty()) {
            genderForFilterRepository.deleteAllByUserId(userId);
        }
        List<GenderForFilter> genderList = dto.getGender();
        for (GenderForFilter n : genderList)
            genderForFilterRepository.save(filterConverter.filterSave(user, n));

        if (!mbtiForFilterRepository.findAllByUser(userId).isEmpty()) {
            mbtiForFilterRepository.deleteAllByUserId(userId);
        }
        List<MbtiForFilter> mbtiBlockedList = dto.getBlockedMbti();
        for (MbtiForFilter i : mbtiBlockedList)
            mbtiForFilterRepository.save(filterConverter.mbtiFiltersave(user, i));

        if (!majorForFilterRepository.findAllByUser(userId).isEmpty()) {
            majorForFilterRepository.deleteAllByUserId(userId);
        }
        List<MajorForFilter> majorBlockedList = dto.getBlockedMajor();
        for (MajorForFilter j : majorBlockedList)
            majorForFilterRepository.save(filterConverter.majorFiltersave(user, j));

        if (!gradeForFilterRepository.findAllByUser(userId).isEmpty()) {
            gradeForFilterRepository.deleteAllByUserId(userId);
        }
        List<GradeForFilter> gradeBlockedList = dto.getGrade();
        for (GradeForFilter k : gradeBlockedList)
            gradeForFilterRepository.save(filterConverter.gradeFiltersave(user, k));

        List<User> userList = userRepository.findTrueStudentByFilter(userId);

        return userRepository.findTrueStudentByFilter(userId)
                .stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());

    }

    // 필터 저장 후 필터 적용된 재학생 리스트 반환
    public List<UserListResponseDto> FalseList(long userId) {
        return userRepository.findFalseStudentByFilter(userId)
                .stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 기존에 지정한 필터 값 조회
    public UserFilterResponseDto selectFilter(long userId) {
        User userFilter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        User dto = userRepository.findFilterByUserId(userId);
        return UserFilterResponseDto.of(dto);

    }
}
