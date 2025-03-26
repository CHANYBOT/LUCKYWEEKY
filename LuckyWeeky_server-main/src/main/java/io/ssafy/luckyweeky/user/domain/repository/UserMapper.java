package io.ssafy.luckyweeky.user.domain.repository;

import io.ssafy.luckyweeky.user.domain.model.UserEntity;
import io.ssafy.luckyweeky.user.domain.model.UserSaltEntity;

import java.util.Map;

public interface UserMapper {
    UserEntity findById(long userId);
    UserEntity findByEmail(String email);
    void insertUser(UserEntity user);
    void insertUserSalt(UserSaltEntity userSaltEntity);
    String findSaltByEmail(String email);
    void insertUserToken(long userId);
    int updateUserToken(Map<String,Object> params);
    String findTokenById(long userId);
    void deleteTokenById(Long userId);
}
