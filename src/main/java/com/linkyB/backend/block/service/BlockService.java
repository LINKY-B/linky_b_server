package com.linkyB.backend.block.service;

import com.linkyB.backend.block.converter.BlockConverter;
import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.dto.PatchBlockReq;
import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.repository.BlockRepository;
import com.linkyB.backend.match.repository.MatchRepository;
import com.linkyB.backend.match.service.MatchService;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.dto.UserResponseDto;
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
public class BlockService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final BlockConverter blockconverter;

    private final MatchRepository matchRepository;

    // 유저 차단
    @Transactional
    public BlockDto userBlock(long userId, long userGetBlocked) {
        User giveUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User getUser = userRepository.findById(userGetBlocked).orElseThrow(UserNotFoundException::new);

        // 차단 내역이 이미 존재하는 경우, 상태만 활성화되게 설정해준다.
        Optional<Block> blockOptional = blockRepository.findByUserGiveBlockAndUserGetBlocked(giveUser, getUser);
        if (blockOptional.isPresent()) {
            Block block = blockOptional.get();
            block.setActive(true);
            return BlockDto.of(block);
        }

        Block entity = blockRepository.save(blockconverter.block(giveUser, getUser));
        return BlockDto.of(entity);
    }

    // 차단 해제
    @Transactional
    public UserResponseDto cancelBlock(Long userId, PatchBlockReq dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<Long> targetIds = dto.getTargetUserIds();
        List<Block> blocks = blockRepository.getBlocks(userId, targetIds);

        for(Block b: blocks){
            b.setActive(false);
        }

        return UserResponseDto.of(user);
    }

    // 차단 리스트 조회
    public List<UserListResponseDto> BlockList(long userId) {
        return userRepository.findAllByUserGiveBlock(userId).stream().map(UserListResponseDto::new).collect(Collectors.toList());
    }
}
