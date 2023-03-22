package com.linkyB.backend.home.controller;

import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.filter.dto.PostFilterReq;
import com.linkyB.backend.filter.dto.UserFilterDto;
import com.linkyB.backend.filter.service.FilterService;
import com.linkyB.backend.home.service.HomeService;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.dto.UserListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linkyB.backend.common.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;
    private final FilterService filterService;
    private final SecurityUtils securityUtils;

    /*
     * 재학생, 졸업생 유저 리스트 2개 조회
     * 필터 디폴트 값
     */
    @GetMapping("/all")
    public ResultResponse<Map<String, List<UserListDto>>> HomeList(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                                   @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Map<String, List<UserListDto>> response = new HashMap<>();
        response.put("졸업생 유저 리스트", homeService.TrueList(offset, limit, securityUtils.getCurrentUserId()));
        response.put("재학생 유저 리스트", homeService.FalseList(offset, limit, securityUtils.getCurrentUserId()));


        return new ResultResponse<>(GET_HOME_LIST_SUCCESS, response);
    }

    // 필터 지정 후 필터 적용된 리스트 2개 반환
    @PostMapping("/filter/save")
    public ResultResponse<Map<String, List<UserListDto>>> saveFilter(@RequestBody PostFilterReq dto) {

        Map<String, List<UserListDto>> response = new HashMap<>();
        response.put("졸업생 유저 리스트", filterService.TrueList(securityUtils.getCurrentUserId(), dto));
        response.put("재학생 유저 리스트", filterService.FalseList(securityUtils.getCurrentUserId()));

        return new ResultResponse<>(GET_HOME_LIST_SUCCESS, response);
    }

    // 기존에 지정한 필터 내용 조회
    @GetMapping("/filter")
    public ResultResponse<UserFilterDto> selectFilter() {
        UserFilterDto response = filterService.selectFilter(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_FILTER_SUCCESS, response);
    }


    // 닉네임 검색 기능
    @GetMapping("/search")
    public ResultResponse<List<UserListDto>> SearchByNickName(@RequestParam(value = "nickName") String nickName) {
        List<UserListDto> response = homeService.search(nickName);
        return new ResultResponse<>(GET_SEARCH_NICKNAME_SUCCESS, response);
    }
}
