package io.ssafy.luckyweeky.user.infrastructure.repository;

import io.ssafy.luckyweeky.common.infrastructure.persistence.MyBatisSqlSessionFactory;
import io.ssafy.luckyweeky.user.domain.model.UserEntity;
import io.ssafy.luckyweeky.user.domain.model.UserSaltEntity;
import io.ssafy.luckyweeky.user.domain.repository.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.util.Map;

public class UserRepository {
    private final SqlSessionFactory sqlSessionFactory;

    public UserRepository() {
        this.sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
    }

    // user_id로 사용자 조회
    public UserEntity findById(long userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.findById(userId);
        }
    }

    // 이메일로 사용자 조회
    public UserEntity findByEmail(String email) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.findByEmail(email);
        }
    }

    // 사용자 추가
    public boolean insertUser(UserEntity userEntity, UserSaltEntity userSalt) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.insertUser(userEntity);
            mapper.insertUserSalt(userSalt);
            mapper.insertUserToken(userEntity.getUserId());
            session.commit(); // 트랜잭션 커밋
            return true;
        }catch (RuntimeException e) {
            return false;
        }
    }

    public String getUserSalt(String email) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.findSaltByEmail(email);
        }
    }

    public boolean updateRefreshToken(long userId, String token) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.updateUserToken(Map.of(
                    "userId",userId,
                    "token",token
            ));
            session.commit();
            return true;
        }catch (RuntimeException e) {
            return false;
        }
    }

    public String getUserToken(long userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.findTokenById(userId);
        }
    }

    public void deleteTokenById(Long userId) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.deleteTokenById(userId);
        }
    }
}
