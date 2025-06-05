package com.welab.backend_user.api.backend;

import com.welab.backend_user.common.dto.ApiResponseDto;
import com.welab.backend_user.domain.dto.SiteUserInfoDto;
import com.welab.backend_user.domain.dto.UserInfoDto;
import com.welab.backend_user.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/backend/user/v1")
public class BackendUserController {

    private final SiteUserService siteUserService;

    @PostMapping(value = "/user/info")
    public UserInfoDto.Response info(@RequestBody UserInfoDto.Request request) {
        log.info("userInfo: userId = {}", request.getUserId());

        UserInfoDto.Response response = new UserInfoDto.Response();
        response.setUserId(request.getUserId());
        response.setUserName(request.getUserId());
        response.setPhoneNumber("010-0000-0000");

        return response;
    }

    @GetMapping(value = "/user/{userId}")
    public ApiResponseDto<SiteUserInfoDto> userInfo(@PathVariable String userId) {
        SiteUserInfoDto userInfoDto = siteUserService.userInfo(userId);
        return ApiResponseDto.createOK(userInfoDto);
    }
}
