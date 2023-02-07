package com.linkyB.backend.block.service;

import com.linkyB.backend.block.converter.BlockConverter;
import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.dto.PatchBlockReq;
import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.mapper.BlockMapper;
import com.linkyB.backend.block.repository.BlockRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.mapper.UserMapper;
import com.linkyB.backend.user.presentation.dto.UserDto;
import com.linkyB.backend.user.presentation.dto.UserListDto;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        User Give = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
        User Get = userRepository.findById(userGetBlocked)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        Block entity = blockRepository.save(blockconverter.block(Give, Get));
        BlockDto dto = BlockMapper.INSTANCE.entityToDto(entity);

        return dto;
    }

    // 차단 해제
    @Transactional
    public UserDto cancleBlock(long userId, PatchBlockReq dto) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));


        List<Long> blockList = dto.getBlockId();
        for (Long blockId : blockList)
            blockRepository.updateStatus(blockId);

        UserDto response = UserMapper.INSTANCE.entityToDto(users);
        return response;

    }

    // 차단 리스트 조회
    public List<UserListDto> BlockList(long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        List<User> users = userRepository.findAllByUserGiveBlock(userId);
        List<UserListDto> dto = UserMapper.INSTANCE.entityToDtoList(users);
        return dto;
    }
}
