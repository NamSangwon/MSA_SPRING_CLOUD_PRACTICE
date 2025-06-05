package com.welab.backend_user.domain.dto;

import com.welab.backend_user.common.exception.BadParameter;
import com.welab.backend_user.domain.SiteUser;
import com.welab.backend_user.secret.hash.SecureHashUtils;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUserRegisterDto {
    @NotBlank(message = "아이디를 입력하세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

//    @NotBlank(message = "전화번호를 입력하세요.")
    private String phoneNumber;

    @Min(value = 1, message = "나이는 1살 이상이어야 합니다.")
    @Max(value = 200, message = "나이는 200살 이하이어야 합니다.")
    private Integer age;

    public void validate() {
        if (age >= 20 && phoneNumber == null) {
            throw new BadParameter("20살 이상인 경우, 전화번호를 입력해야 합니다.");
        }
    }

    public SiteUser toEntity() {
        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(this.userId);
        siteUser.setPhoneNumber(this.phoneNumber);
        // TODO: SHA1 또는 SHA256으로 password를 해시 값으로 변환
        String hashedPassword = SecureHashUtils.hash(this.password);
        siteUser.setPassword(hashedPassword);
        return siteUser;
    }
}