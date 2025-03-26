package io.ssafy.luckyweeky.user.application.converter;

import io.ssafy.luckyweeky.common.implement.Converter;
import io.ssafy.luckyweeky.common.util.security.OpenCrypt;
import io.ssafy.luckyweeky.common.util.generator.SnowflakeIdGenerator;
import io.ssafy.luckyweeky.user.application.dto.GeneralSignupUserDto;
import io.ssafy.luckyweeky.user.domain.model.UserEntity;

public class GeneralSignupUserDtoToUserEntityWithSaltConverter implements Converter<GeneralSignupUserDto, UserEntityWithSalt> {
    private static final GeneralSignupUserDtoToUserEntityWithSaltConverter INSTANCE = new GeneralSignupUserDtoToUserEntityWithSaltConverter();

    private GeneralSignupUserDtoToUserEntityWithSaltConverter() {}

    public static GeneralSignupUserDtoToUserEntityWithSaltConverter getInstance() {
        return INSTANCE;
    }
    @Override
    public UserEntityWithSalt convert(GeneralSignupUserDto generalSignupUser) {
        String salt = OpenCrypt.createEncryptSalt();
        UserEntity userEntity = new UserEntity.Builder()
                .userId(SnowflakeIdGenerator.getInstance().nextId())
                .passwordHash(OpenCrypt.getEncryptPassword(generalSignupUser.getPassword(), salt))
                .username(generalSignupUser.getUsername())
                .email(generalSignupUser.getEmail())
                .birthDate(generalSignupUser.getBirthDate())
                .profileImageKey(generalSignupUser.getProfileImageKey())
                .build();

        return new UserEntityWithSalt(userEntity, salt);
    }
}
