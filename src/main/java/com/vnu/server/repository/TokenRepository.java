package com.vnu.server.repository;

import com.vnu.server.cache.CacheStore;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class TokenRepository {
    private final CacheStore<String> cacheStore = new CacheStore<>(60 * 24, TimeUnit.SECONDS);
    public String generateToken(String email) {
        String token = UUID.randomUUID().toString();
        cacheStore.add(email, token);
        return token;
    }

    public String get(String email) throws NullPointerException{
        return cacheStore.get(email);
    }
    public void remove(String email) {
        cacheStore.clear(email);
    }
}