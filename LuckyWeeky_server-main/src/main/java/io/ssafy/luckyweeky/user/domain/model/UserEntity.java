
package io.ssafy.luckyweeky.user.domain.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserEntity implements Serializable {
    private long userId;                  // 유저 고유 ID
    private String username;             // 사용자 이름
    private String email;                // 이메일
    private String passwordHash;         // 비밀번호 해시값
    private String oauthProvider;        // OAuth 제공자 (예: Google)
    private String oauthId;              // OAuth 제공자가 발급한 고유 사용자 ID
    private LocalDate birthDate;         // 생년월일
    private String profileImageKey;      // 프로필 이미지 URL
    private LocalDateTime lastLoginAt;   // 마지막 로그인 시간
    private LocalDateTime createdAt;     // 생성 날짜
    private LocalDateTime updatedAt;     // 업데이트 날짜

    public UserEntity() {}

    // Private Constructor
    private UserEntity(Builder builder) {
        this.userId = builder.userId;
        this.username = builder.username;
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.oauthProvider = builder.oauthProvider;
        this.oauthId = builder.oauthId;
        this.birthDate = builder.birthDate;
        this.profileImageKey = builder.profileImageKey;
        this.lastLoginAt = builder.lastLoginAt;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // Builder Class
    public static class Builder {
        private long userId;
        private String username;
        private String email;
        private String passwordHash;
        private String oauthProvider;
        private String oauthId;
        private LocalDate birthDate;
        private String profileImageKey;
        private LocalDateTime lastLoginAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder oauthProvider(String oauthProvider) {
            this.oauthProvider = oauthProvider;
            return this;
        }

        public Builder oauthId(String oauthId) {
            this.oauthId = oauthId;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder profileImageKey(String profileImageKey) {
            this.profileImageKey = profileImageKey;
            return this;
        }

        public Builder lastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfileImageKey() {
        return profileImageKey;
    }

    public void setProfileImageKey(String profileImageKey) {
        this.profileImageKey = profileImageKey;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
