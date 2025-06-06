package com.welab.backend_user.api.open;

import com.welab.backend_user.common.dto.ApiResponseDto;
import com.welab.backend_user.remote.alim.RemoteAlimService;
import com.welab.backend_user.remote.alim.dto.AlimSendSmsDto;
import com.welab.backend_user.common.web.context.GatewayRequestHeaderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/user/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final RemoteAlimService remoteAlimService;

    @GetMapping(value = "/test")
    public ApiResponseDto<String> test() {
//        String response =  remoteAlimService.callAlimHello();
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();

        return ApiResponseDto.createOK(userId);
    }

    @PostMapping(value = "/sms")
    public ApiResponseDto<AlimSendSmsDto.Response> sms(@RequestBody AlimSendSmsDto.Request request) {
        var response = remoteAlimService.sendSms(request);
        return ApiResponseDto.createOK(response);
    }
}
