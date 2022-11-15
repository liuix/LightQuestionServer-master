package com.zyh.lightquestionserver.server;

public interface RedisService {
    void set(String key, String value);

    void set(String key, String value, Long time);

    String get(String key);

    boolean delete(String key);

    Long getExpireTime(String key);
}
