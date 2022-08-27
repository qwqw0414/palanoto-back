package com.joje.palanoto.repository.config;

import com.joje.palanoto.model.entity.config.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<ConfigEntity, String> {
}