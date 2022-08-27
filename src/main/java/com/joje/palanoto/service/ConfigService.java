package com.joje.palanoto.service;

public interface ConfigService {
    Object get(String key);

    void set(String key, Object value);
}
