package com.LionKing.Teamply.domain.user.entity;

import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String position;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Lob
    private String bio;

    @Lob
    private String career;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // 회원가입 이후 마이페이지에서 정보 수정할 때 쓸 메서드
    public void updateProfile(String name, String position, String profileImageUrl, String bio, String career) {
        if (name != null) this.name = name;
        if (position != null) this.position = position;
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
        if (bio != null) this.bio = bio;
        if (career != null) this.career = career;
    }

    // 비밀번호 변경 시 사용
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}