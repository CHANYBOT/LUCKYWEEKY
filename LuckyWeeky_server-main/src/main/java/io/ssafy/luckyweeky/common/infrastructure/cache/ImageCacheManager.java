package io.ssafy.luckyweeky.common.infrastructure.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageCacheManager {
    private static ImageCacheManager instance;
    private final Map<String, byte[]> cache;

    private ImageCacheManager() {
        this.cache = new ConcurrentHashMap<>();
    }

    public static synchronized ImageCacheManager getInstance() {
        if (instance == null) {
            instance = new ImageCacheManager();
        }
        return instance;
    }

    // 캐시 저장
    public void addToCache(String key, byte[] imageData) {
        cache.put(key, imageData);
    }

    // 캐시에서 가져오기
    public byte[] getFromCache(String key) {
        return cache.get(key);
    }

    // 캐시에서 삭제
    public void removeFromCache(String key) {
        cache.remove(key);
    }

    // 캐시 비우기
    public void clearCache() {
        cache.clear();
    }
}
