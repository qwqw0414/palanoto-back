package com.joje.palanoto.repository.account;

import com.joje.palanoto.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    long countByUserId(String userId);
}