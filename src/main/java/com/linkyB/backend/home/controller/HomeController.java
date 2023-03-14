package com.linkyB.backend.home.controller;

import com.linkyB.backend.config.BaseResponse;
import com.linkyB.backend.filter.dto.UserFilterDto;
import com.linkyB.backend.filter.dto.PostFilterReq;
import com.linkyB.backend.filter.service.FilterService;
import com.linkyB.backend.home.service.HomeService;
import com.linkyB.backend.user.jwt.JwtTokenProvider;
import com.linkyB.backend.user.presentation.dto.UserListDto;
import com.linkyB.backend.user.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;
    private final FilterService filterService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
     * 재학생, 졸업생 유저 리스트 2개 조회
     * 필터 디폴트 값
     */
    @GetMapping("/all")
    public BaseResponse<Map<String, List<UserListDto>>> HomeList(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                                 @RequestParam(value = "limit", defaultValue = "20") int limit
                                                                   ) {
        Map<String, List<UserListDto>> response = new HashMap<>();
        long TokenUser = jwtTokenProvider.getUser();
        response.put("졸업생 유저 리스트", homeService.TrueList(offset, limit, TokenUser));
        response.put("재학생 유저 리스트", homeService.FalseList(offset, limit, TokenUser));

        return new BaseResponse<>(response);
    }

    // 필터 지정 후 필터 적용된 리스트 2개 반환
    @PostMapping("/filter/save")
    public BaseResponse<Map<String, List<UserListDto>>> saveFilter(@RequestBody PostFilterReq dto) {

        Map<String, List<UserListDto>> response = new HashMap<>();
        long TokenUser = jwtTokenProvider.getUser();
        response.put("졸업생 유저 리스트", filterService.TrueList(TokenUser, dto));
        response.put("재학생 유저 리스트", filterService.FalseList(TokenUser));

        return new BaseResponse<>(response);
    }

    // 기존에 지정한 필터 내용 조회
    @GetMapping("/filter")
    public BaseResponse<UserFilterDto> selectFilter() {
        long TokenUser = jwtTokenProvider.getUser();
        UserFilterDto response = filterService.selectFilter(TokenUser);
        return new BaseResponse<>(response);
    }


    // 닉네임 검색 기능
    @GetMapping("/search")
    public BaseResponse<List<UserListDto>> SearchByNickName(@RequestParam(value ="nickName")String nickName) {
        List<UserListDto> response = homeService.search(nickName);
        return new BaseResponse<>(response);
    }
}
