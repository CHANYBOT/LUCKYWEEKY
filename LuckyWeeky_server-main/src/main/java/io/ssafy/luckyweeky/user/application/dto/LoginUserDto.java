package io.ssafy.luckyweeky.user.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoginUserDto {
    private long userId;                  // 유저 고유 ID
    private String username;             // 사용자 이름
    private String email;                // 이메일
    private String password;         // 비밀번호 해시값
    private LocalDate birthDate;         // 생년월일
    private String profileImageKey;      // 프로필 이미지 URL

    public LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginUserDto(long userId, String username, String email, LocalDate birthDate, String profileImageKey) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = "";
        this.birthDate = birthDate;
        this.profileImageKey = profileImageKey;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getProfileImageKey() {
        return profileImageKey;
    }
}
