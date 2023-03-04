package com.vnu.server.repository;

import com.vnu.server.cache.CacheStore;
import com.vnu.server.entity.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class TokenRepository {
    private final CacheStore<User> cacheStore = new CacheStore<>(60 * 60, TimeUnit.SECONDS);
    public String generateToken(String key, User user) {
        String token = UUID.randomUUID().toString();
        cacheStore.add(key, user);
        return token;
    }

    public User get(String key) throws NullPointerException{
        return cacheStore.get(key);
    }
    public void remove(String key) {
        cacheStore.clear(key);
    }
}