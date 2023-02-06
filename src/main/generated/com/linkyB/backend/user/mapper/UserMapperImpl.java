package com.linkyB.backend.user.mapper;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.presentation.dto.UserDetailDto;
import com.linkyB.backend.user.presentation.dto.UserDetailDto.UserDetailDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-07T00:52:34+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDetailDto UserdetaildtoToEntity(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserDetailDtoBuilder userDetailDto = UserDetailDto.builder();

        userDetailDto.userId( entity.getUserId() );
        userDetailDto.userNickName( entity.getUserNickName() );
        userDetailDto.userMajorName( entity.getUserMajorName() );
        userDetailDto.userStudentNum( entity.getUserStudentNum() );
        userDetailDto.userBirth( entity.getUserBirth() );
        userDetailDto.userSex( entity.getUserSex() );
        userDetailDto.userSelfIntroduction( entity.getUserSelfIntroduction() );
        userDetailDto.userProfileImg( entity.getUserProfileImg() );
        userDetailDto.userMBTI( entity.getUserMBTI() );
        List<Interest> list = entity.getUserInterest();
        if ( list != null ) {
            userDetailDto.userInterest( new ArrayList<Interest>( list ) );
        }
        List<Personality> list1 = entity.getUserPersonality();
        if ( list1 != null ) {
            userDetailDto.userPersonality( new ArrayList<Personality>( list1 ) );
        }
        userDetailDto.userLikeCount( entity.getUserLikeCount() );

        return userDetailDto.build();
    }
}
