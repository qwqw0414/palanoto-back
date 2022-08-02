package com.joje.palanoto.repository.account;

import com.joje.palanoto.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    long countByUserId(String userId);

    Optional<UserEntity> findByUserId(String userId);
}