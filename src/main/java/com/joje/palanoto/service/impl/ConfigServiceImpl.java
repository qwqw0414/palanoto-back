package com.joje.palanoto.service.impl;

import com.joje.palanoto.model.entity.config.ConfigEntity;
import com.joje.palanoto.repository.config.ConfigRepository;
import com.joje.palanoto.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service("ConfigService")
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;

    @Override
    public String get(String key) {
        ConfigEntity config = configRepository.findById(key).orElse(new ConfigEntity());
        return config.getValue();
    }



    @Override
    public void set(String key, Object value) {
        ConfigEntity configEntity = new ConfigEntity(key, String.valueOf(value));
        log.debug("[configEntity]=[{}]", configEntity);
        configRepository.save(configEntity);
    }

}
