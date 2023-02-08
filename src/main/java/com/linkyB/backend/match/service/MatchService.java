package com.linkyB.backend.match.service;

import com.linkyB.backend.block.converter.BlockConverter;
import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.mapper.BlockMapper;
import com.linkyB.backend.block.repository.BlockRepository;
import com.linkyB.backend.chat.converter.ChattingConverter;
import com.linkyB.backend.chat.entity.ChattingRoom;
import com.linkyB.backend.chat.repository.ChattingRoomRepository;
import com.linkyB.backend.match.converter.MatchConverter;
import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.dto.MatchListDto;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.match.entity.MatchStatus;
import com.linkyB.backend.match.entity.status;
import com.linkyB.backend.match.mapper.MatchMapper;
import com.linkyB.backend.match.repository.MatchRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.mapper.UserMapper;
import com.linkyB.backend.user.presentation.dto.UserListDto;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public MatchDto matching(long userId, long userGetMatched) {

        User Matching = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
        User GetMatched = userRepository.findById(userGetMatched)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        Match entity = matchRepository.save(matchConverter.tryMatching(Matching, GetMatched));
        MatchDto dto = MatchMapper.INSTANCE.entityToDto(entity);

        return dto;
    }

    // 매칭 수락
    @Transactional
    public MatchDto accept(long matchId, long userId) {

        Match entity = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("해당 연결내역이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        // userId와 매칭을 받은 유저의 인덱스가 같다면 수락 처리
        if (entity.getUserMatching().getUserId() == user.getUserId()) {
            entity.update(MatchStatus.ACTIVE);
            MatchDto dto = MatchMapper.INSTANCE.entityToDto(entity);

            // 채팅 테이블 입력
            ChattingRoom chat = chattingRoomRepository.save(chattingConverter.createChat(entity.getUserGetMatched(), entity.getUserMatching()));

            return dto;

            // 다르다면 예외처리
        } else
            throw new RuntimeException("수락 권한이 없습니다.");

    }

    // 매칭 거절
    @Transactional
    public BlockDto refuse(long matchId, long userId) {

        Match entity = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("해당 연결내역이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        // userId와 매칭을 받은 유저의 인덱스가 같다면 수락 처리
        if (entity.getUserMatching().getUserId() == user.getUserId()) {
            entity.updateMatch(status.INACTIVE);

            Block block = blockRepository.save(blockConverter.block(entity.getUserGetMatched(), entity.getUserMatching()));
            BlockDto dto = BlockMapper.INSTANCE.entityToDto(block);
            return dto;
        } else
            throw new RuntimeException("거절 권한 없습니다.");
    }

    // 매칭 모두 수락
    @Transactional
    public MatchListDto all(long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        matchRepository.updateUserByUserGetMatched(userId);
        MatchListDto match = matchConverter.MatchAllokResponseDto(userId);

        return match;
    }

    // 내가 매칭 시도한 내역 삭제
    @Transactional
    public BlockDto blockMatch(long matchId, long userId) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("해당 연결내역이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        if(match.getUserMatching().getUserId() == user.getUserId()) {
            match.updateMatch(status.INACTIVE);
            match.update(MatchStatus.INACTIVE);

            Block block = blockRepository.save(blockConverter.blockGetMatched(match.getUserMatching(), match.getUserGetMatched()));
            BlockDto dto = BlockMapper.INSTANCE.entityToDto(block);
            return dto;
        }
        throw new RuntimeException("삭제 권한 없습니다.");
    }

    // 나에게 매칭 시도한 유저 전체 조회
    public List<UserListDto> GetMatchedList(long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        List<User> users = userRepository.findAllByUserGetMatched(userId);
        List<UserListDto> dto = UserMapper.INSTANCE.entityToDtoList(users);

        return dto;
    }

    // 내가 매칭 시도한 유저 전체 조회
    public List<UserListDto> MatchingList(long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        List<User> users = userRepository.findAllByUserMatching(userId);
        List<UserListDto> dto = UserMapper.INSTANCE.entityToDtoList(users);

        return dto;
    }

    // 나에게 연결을 시도한 회원 -> getMatched == 나
    public List<UserListDto> homeGetMatchedList(long userId) {
        List<User> userList = userRepository.findTop4ByUserGetMatched(userId);
        List<UserListDto> listdto = UserMapper.INSTANCE.entityToDtoList(userList);

        return listdto;
    }

    // 내가 연결을 시도한 회원 -> Matching == 나
    public List<UserListDto> homeMatchingList(long userId) {
        List<User> userList = userRepository.findTop4ByUserMatching(userId);
        List<UserListDto> listdto = UserMapper.INSTANCE.entityToDtoList(userList);

        return listdto;
    }

}
