package io.ssafy.luckyweeky.user.application.dto;

import java.time.LocalDate;

public class GeneralSignupUserDto {
    private String email;                // 이메일
    private String password;             // 비밀번호
    private String username;             // 사용자 이름
    private LocalDate birthDate;         // 생년월일
    private String profileImageKey;      // 프로필 이미지 URL

    public GeneralSignupUserDto(String email, String password, String username, String birthDate, String profileImageKey) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthDate = LocalDate.parse(birthDate);
        this.profileImageKey = profileImageKey;
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
