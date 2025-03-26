package io.ssafy.luckyweeky.user.application.converter;

import io.ssafy.luckyweeky.common.implement.Converter;
import io.ssafy.luckyweeky.user.application.dto.LoginUserDto;
import io.ssafy.luckyweeky.user.domain.model.UserEntity;

public class UserEntityToLoginUserDto implements Converter<UserEntity, LoginUserDto> {
    private final static UserEntityToLoginUserDto INSTANCE = new UserEntityToLoginUserDto();

    public static UserEntityToLoginUserDto getInstance() {
        return INSTANCE;
    }

    @Override
    public LoginUserDto convert(UserEntity source) {
        return new LoginUserDto(
                source.getUserId(),
                source.getUsername(),
                source.getEmail(),
                source.getBirthDate(),
                source.getProfileImageKey()
        );
    }
}
