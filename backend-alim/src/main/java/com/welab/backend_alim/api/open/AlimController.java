package com.welab.backend_alim.api.open;

import com.welab.backend_alim.remote.user.RemoteUserService;
import com.welab.backend_alim.remote.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/alim/v1")
public class AlimController {

    private final RemoteUserService remoteUserService;

    @PostMapping(value = "/user/info")
    public UserInfoDto.Response userInfo(@RequestBody UserInfoDto.Request request) {
        var response = remoteUserService.userInfo(request); // [제한적] 타입을 추론할 수 있는 경우, 개발 편의성을 위해 사용 (타입은 명확히 지정됨)
        return response;
    }
}
