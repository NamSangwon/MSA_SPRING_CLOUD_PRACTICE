package com.welab.backend_user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "site_user")
public class SiteUser {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    // 안바뀔 확률이 100%가 아니기 때문에 그냥 id를 PK로 사용
    @Column(name="user_id", unique = true, nullable = false)
    @Getter @Setter
    private String userId;

    @Column(name="password", nullable = false)
    @Getter @Setter
    private String password;

    @Column(name = "phone_number", nullable = false)
    @Getter @Setter
    private String phoneNumber;

    // 실제로 DB에서 삭제시키지 않고, flag로 명시
    @Column(name = "deleted", nullable = false)
    @Getter @Setter
    private Boolean deleted = false;
}
