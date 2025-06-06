package com.welab.backend_alim.api.backend;

import com.welab.backend_alim.domain.dto.SendSmsDto;
import com.welab.backend_alim.remote.user.RemoteUserService;
import com.welab.backend_alim.remote.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/backend/alim/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class BackendAlimController {

    private final RemoteUserService remoteUserService;

    @GetMapping(value="/hello")
    public String hello() {
        return "알림 백엔드 서비스가 호출되었습니다.";
    }

    @PostMapping(value = "/sms")
    public SendSmsDto.Response sendSms(@RequestBody SendSmsDto.Request request) {
        log.info("sendSms: userId = {}", request.getUserId());

        SendSmsDto.Response response = new SendSmsDto.Response();
        response.setResult("OK");

        return response;
    }
}
