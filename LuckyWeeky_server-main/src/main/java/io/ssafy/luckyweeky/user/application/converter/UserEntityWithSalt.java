package io.ssafy.luckyweeky.user.application.converter;

import io.ssafy.luckyweeky.user.domain.model.UserEntity;

public class UserEntityWithSalt {
    private final UserEntity userEntity;
    private final String salt;

    public UserEntityWithSalt(UserEntity userEntity, String salt) {
        this.userEntity = userEntity;
        this.salt = salt;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public String getSalt() {
        return salt;
    }
}
