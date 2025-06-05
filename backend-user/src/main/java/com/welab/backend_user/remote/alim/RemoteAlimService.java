package com.welab.backend_user.remote.alim;

import com.welab.backend_user.remote.alim.dto.AlimSendSmsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// RestTemplate 대체
// backend-alim의 Controller와 매핑
@FeignClient(name = "backend-alim", path = "/backend/alim/v1")
public interface RemoteAlimService {
    // /backend/alim/v1/hello 호출
    @GetMapping(value = "/hello")
    String callAlimHello();
    
    // /backend/alim/v1/sms 호출
    @PostMapping(value = "/sms")
    AlimSendSmsDto.Response sendSms(AlimSendSmsDto.Request request);
}