package com.linkyB.backend.match.service;

import com.linkyB.backend.block.converter.BlockConverter;
import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.repository.BlockRepository;
import com.linkyB.backend.chat.converter.ChattingConverter;
import com.linkyB.backend.chat.repository.ChattingRoomRepository;
import com.linkyB.backend.match.converter.MatchConverter;
import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.dto.MatchListDto;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.match.exception.MatchNotFoundException;
import com.linkyB.backend.match.repository.MatchRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.dto.UserListResponseDto;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final BlockRepository blockRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingConverter chattingConverter;
    private final MatchConverter matchConverter;
    private final BlockConverter blockConverter;

    // 매칭 시도
    @Transactional
    public MatchDto matching(long tryMatchUserId, long userGetMatched) {
        User Matching = userRepository.findById(tryMatchUserId).orElseThrow(MatchNotFoundException::new);
        User GetMatched = userRepository.findById(userGetMatched).orElseThrow(MatchNotFoundException::new);

        // 기존 매칭 내역이 존재하는 경우, 해당 내역만 다시 활성화 함.
        Optional<Match> matchResult = matchRepository.findMatchByTryMatchingUserAndMatchedUser(tryMatchUserId, userGetMatched);
        if (matchResult.isPresent()) {
            Match match = matchResult.get();
            match.activateMatch();
            return MatchDto.of(match);
        }

        Match entity = matchRepository.save(matchConverter.tryMatching(Matching, GetMatched));
        return MatchDto.of(entity);
    }

    // 매칭 수락
    @Transactional
    public MatchDto accept(long getMatchedUserId, long tryMatchUserId) {
        User getMatchedUser = userRepository.findById(getMatchedUserId).orElseThrow(UserNotFoundException::new);
        Match match = matchRepository.findMatchByTryMatchingUserAndMatchedUser(tryMatchUserId, getMatchedUserId).orElseThrow(MatchNotFoundException::new);

        match.approve();
        getMatchedUser.increaseMatchCount(); // 매칭 수락된 유저 카운트 +1

        // 채팅 테이블 입력
        chattingRoomRepository.save(chattingConverter.createChat(match.getUserGetMatched(), match.getUserMatching()));
        return MatchDto.of(match);
    }

    // 매칭 거절
    @Transactional
    public BlockDto refuse(long getMatchedUserId, long tryMatchUserId) {
        Match match = matchRepository.findMatchByTryMatchingUserAndMatchedUser(tryMatchUserId, getMatchedUserId).orElseThrow(MatchNotFoundException::new);
        match.refuse();
        Block block = blockRepository.save(blockConverter.block(match.getUserGetMatched(), match.getUserMatching()));
        return BlockDto.of(block);

    }

    // 매칭 모두 수락
    @Transactional
    public MatchListDto all(long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        matchRepository.updateUserByUserGetMatched(userId);
        MatchListDto match = matchConverter.MatchAllokResponseDto(userId);

        return match;
    }

    // 내가 매칭 시도한 내역 삭제
    @Transactional
    public MatchDto blockMatch(long tryMatchUserId, long getMatchedUserId) {
        Match entity = matchRepository.findMatchByTryMatchingUserAndMatchedUser(tryMatchUserId, getMatchedUserId).orElseThrow(MatchNotFoundException::new);

        entity.deleteMatch();
        entity.refuse();

        // 차단까지 하는게 맞나요?

        return MatchDto.of(entity);
    }

    // 나에게 매칭 시도한 유저 전체 조회
    public List<UserListResponseDto> GetMatchedList(long userId) {

        return userRepository.findAllByUserGetMatched(userId)
                .stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 내가 매칭 시도한 유저 전체 조회
    public List<UserListResponseDto> MatchingList(long userId) {
        return userRepository.findAllByUserMatching(userId)
                .stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 나에게 연결을 시도한 회원 -> getMatched == 나
    public List<UserListResponseDto> homeGetMatchedList(long userId) {
        return userRepository.findTop4ByUserGetMatched(userId)
                .stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 내가 연결을 시도한 회원 -> Matching == 나
    public List<UserListResponseDto> homeMatchingList(long userId) {
        return userRepository.findTop4ByUserMatching(userId)
                .stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());
    }
}
