package com.linkyB.backend.home.controller;

import com.linkyB.backend.home.service.HomeService;
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

    /*
     * 재학생, 졸업생 유저 리스트 2개 조회
     * 필터 디폴트 값
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, List<UserListDto>>> HomeList(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                                   @RequestParam(value = "limit", defaultValue = "20") int limit
                                                                   ) {
        Map<String, List<UserListDto>> response = new HashMap<>();
        long userId = SecurityUtil.getCurrentUserId();
        response.put("재학생 유저 리스트", homeService.TrueList(offset, limit, userId));
        response.put("졸업생 유저 리스트", homeService.FalseList(offset, limit, userId));

        return ResponseEntity.ok().body(response);
    }

}