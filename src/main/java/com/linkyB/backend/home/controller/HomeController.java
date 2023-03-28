package com.linkyB.backend.home.controller;

import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.filter.dto.PostFilterReq;
import com.linkyB.backend.filter.dto.UserFilterResponseDto;
import com.linkyB.backend.filter.service.FilterService;
import com.linkyB.backend.home.service.HomeService;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.dto.UserListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "메인 홈 조회", description = "졸업생, 재학생 유저 리스트 2개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "H001 - 홈 리스트 조회에 성공했습니다.")
    })
    @GetMapping("/all")
    public ResultResponse<Map<String, List<UserListResponseDto>>> HomeList(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                                           @RequestParam(value = "limit", defaultValue = "20") int limit) {
        Map<String, List<UserListResponseDto>> response = new HashMap<>();
        response.put("졸업생 유저 리스트", homeService.TrueList(offset, limit, securityUtils.getCurrentUserId()));
        response.put("재학생 유저 리스트", homeService.FalseList(offset, limit, securityUtils.getCurrentUserId()));

        return new ResultResponse<>(GET_HOME_LIST_SUCCESS, response);
    }

    @Operation(summary = "필터 적용 리스트 조회", description = "필터 지정 후 필터 적용된 졸업생, 재학생 유저 리스트 2개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "H001 - 홈 리스트 조회에 성공했습니다.")
    })
    @PostMapping("/filter/save")
    public ResultResponse<Map<String, List<UserListResponseDto>>> saveFilter(@RequestBody PostFilterReq dto) {

        Map<String, List<UserListResponseDto>> response = new HashMap<>();
        response.put("졸업생 유저 리스트", filterService.TrueList(securityUtils.getCurrentUserId(), dto));
        response.put("재학생 유저 리스트", filterService.FalseList(securityUtils.getCurrentUserId()));

        return new ResultResponse<>(GET_HOME_LIST_SUCCESS, response);
    }

    @Operation(summary = "필터 조회", description = "유저가 적용한 필터 내용 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "H002 - 필터 내용 조회에 성공했습니다.")
    })
    @GetMapping("/filter")
    public ResultResponse<UserFilterResponseDto> selectFilter() {
        UserFilterResponseDto response = filterService.selectFilter(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_FILTER_SUCCESS, response);
    }

    @Operation(summary = "닉네임 검색", description = "@RequestParam으로 들어온 닉네임에 해당하는 유저 조회," +
            "nickName을 포함하기만 해도 조회 성공")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "H003 - 닉네임 검색에 성공했습니다.")
    })
    @GetMapping("/search")
    public ResultResponse<List<UserListResponseDto>> SearchByNickName(@RequestParam(value = "nickName") String nickName) {
        List<UserListResponseDto> response = homeService.search(nickName);
        return new ResultResponse<>(GET_SEARCH_NICKNAME_SUCCESS, response);
    }
}
