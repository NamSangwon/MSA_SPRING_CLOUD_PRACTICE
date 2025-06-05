package com.welab.backend_alim.remote.user;

import com.welab.backend_alim.remote.user.dto.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "backend-user", path="/backend/user/v1")
public interface RemoteUserService {
    @PostMapping(value = "/user/info")
    UserInfoDto.Response userInfo(UserInfoDto.Request request);
}
