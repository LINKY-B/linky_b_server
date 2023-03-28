package com.linkyB.backend.block.service;

import com.linkyB.backend.block.converter.BlockConverter;
import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.dto.PatchBlockReq;
import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.repository.BlockRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.dto.UserResponseDto;
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
public class BlockService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final BlockConverter blockconverter;

    // 유저 차단
    @Transactional
    public BlockDto userBlock(long userId, long userGetBlocked) {

        User Give = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User Get = userRepository.findById(userGetBlocked).orElseThrow(UserNotFoundException::new);

        Block entity = blockRepository.save(blockconverter.block(Give, Get));

        return BlockDto.of(entity);
    }

    // 차단 해제
    @Transactional
    public UserResponseDto cancelBlock(long userId, PatchBlockReq dto) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);


        List<Long> blockList = dto.getBlockId();
        for (Long blockId : blockList)
            blockRepository.updateStatus(blockId);

        return UserResponseDto.of(users);

    }

    // 차단 리스트 조회
    public List<UserListResponseDto> BlockList(long userId) {
        return userRepository.findAllByUserGiveBlock(userId).stream().map(UserListResponseDto::new).collect(Collectors.toList());
    }
}
