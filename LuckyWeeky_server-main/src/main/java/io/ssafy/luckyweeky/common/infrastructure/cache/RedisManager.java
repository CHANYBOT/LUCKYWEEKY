package io.ssafy.luckyweeky.common.infrastructure.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

public class RedisManager {
    private static JedisPool pool = null;

    static {
        String redisHost = getPropertyVariable("REDIS_ENDPOINT"); // AWS에서 확인한 엔드포인트
        int redisPort = 6379; // 기본 포트

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50); // 최대 연결 수 설정
        // JedisPool 객체를 생성하여 Redis 연결 풀 초기화
        // 파라미터: 설정(config), 호스트, 포트, 연결 시간 초과(ms), 암호, SSL 사용 여부
        pool = new JedisPool(poolConfig, redisHost, redisPort, 2000, null, false);
    }

    public static JedisPool getPool() {
        return pool;
    }
    private static String getPropertyVariable(String key) {
        String value = System.getProperty(key);
        System.out.println(value);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException("Environment Variable '" + key + "' is empty.");
        }
        return value;
    }

    public static boolean invalidateCacheBasedOnTitle(String prefix) {
        try (Jedis jedis = getPool().getResource()) {
            Set<String> keys = jedis.keys(prefix+":*");
            for (String key : keys) {
                jedis.del(key); // 관련 캐시 삭제
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
