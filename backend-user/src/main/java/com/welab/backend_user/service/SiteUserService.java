package com.welab.backend_user.service;

import com.welab.backend_user.common.exception.BadParameter;
import com.welab.backend_user.common.exception.NotFound;
import com.welab.backend_user.common.type.ActionAndId;
import com.welab.backend_user.domain.SiteUser;
import com.welab.backend_user.domain.dto.SiteUserInfoDto;
import com.welab.backend_user.domain.dto.SiteUserLoginDto;
import com.welab.backend_user.domain.dto.SiteUserRefreshDto;
import com.welab.backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.backend_user.event.producer.KafkaMessageProducer;
import com.welab.backend_user.remote.alim.RemoteAlimService;
import com.welab.backend_user.repository.SiteUserRepository;
import com.welab.backend_user.secret.hash.SecureHashUtils;
import com.welab.backend_user.secret.jwt.TokenGenerator;
import com.welab.backend_user.secret.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteUserService {

    private final SiteUserRepository siteUserRepository;
    private final TokenGenerator tokenGenerator;
    private final RemoteAlimService remoteAlimService;
    private final KafkaMessageProducer kafkaMessageProducer;

//    @Transactional
//    public void register(SiteUserRegisterDto registerDto) {
//        registerDto.validate();
//
//        SiteUser siteUser = registerDto.toEntity();
//
//        siteUserRepository.save(siteUser);
//
//        // // 아래의 코드가 실패할 시, 회원가입이 안됨
//        // // 따라서, 트랜잭션 밖에서 처리해야 하는 경우도 있고, 안에서 해야 하는 경우도 있음.
//
////        AlimSendSmsDto.Request request = AlimSendSmsDto.Request.fromEntity(siteUser);
////        remoteAlimService.sendSms(request);
//
//        // API 호출 => Kafka (Event Driven) 으로 변경
//        SiteUserInfoEvent message = SiteUserInfoEvent.fromEntity("Create", siteUser);
//        kafkaMessageProducer.send(SiteUserInfoEvent.Topic, message);
//    }

    // AOP 적용한 Kafka
    @Transactional
    public ActionAndId registerUserAndNotify(SiteUserRegisterDto registerDto) {
        SiteUser siteUser = registerDto.toEntity();
        siteUserRepository.save(siteUser);
        return ActionAndId.of("Create", siteUser.getId());
    }

    @Transactional(readOnly = true) // 로그인은 조회 쿼리만 사용
    public TokenDto.AccessRefreshToken login(SiteUserLoginDto loginDto) {
        
        // 로그인 실패 원인을 추측하지 못하도록 에러 메시지 통일
        SiteUser user = siteUserRepository.findByUserId(loginDto.getUserId());
        if (user == null) {
            throw new NotFound("아이디 또는 비밀번호를 확인하세요.");
        }
        if (!SecureHashUtils.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadParameter("아이디 또는 비밀번호를 확인하세요.");
        }

        // deviceType 하드 코딩함.
        // TODO: Header에서 deviceType 추출 必
        return tokenGenerator.generateAccessRefreshToken(loginDto.getUserId(), "WEB");
    }
    
    @Transactional(readOnly = true) // DB 업데이트 안하므로
    public TokenDto.AccessToken refresh(SiteUserRefreshDto refreshDto) {
        String userId = tokenGenerator.validateJwtToken(refreshDto.getToken());
        if (userId == null) {
            throw new BadParameter("토큰이 유효하지 않습니다.");
        }

        SiteUser user = siteUserRepository.findByUserId(userId);
        if (user == null) {
            throw new NotFound("사용자를 찾을 수 없습니다.");
        }

        // TODO: Header에서 deviceType 추출 必
        return tokenGenerator.generateAccessToken(userId, "WEB");
    }

    @Transactional(readOnly = true)
    public SiteUserInfoDto userInfo(String userId) {
        SiteUser user = siteUserRepository.findByUserId(userId);
        if (user == null) {
            throw new NotFound("사용자를 찾을 수 없습니다.");
        }

        return SiteUserInfoDto.fromEntity(user);
    }
}
