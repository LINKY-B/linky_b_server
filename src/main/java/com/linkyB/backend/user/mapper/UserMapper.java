package com.linkyB.backend.user.mapper;

import com.linkyB.backend.filter.dto.UserFilterDto;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.dto.UserDetailDto;
import com.linkyB.backend.user.dto.UserDto;
import com.linkyB.backend.user.dto.UserListDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(User entity);

    List<UserListDto> entityToDtoList(List<User> entity);

    UserDetailDto UserdetaildtoToEntity(User entity);

    UserFilterDto entityToFilterDto(User entity);
}
