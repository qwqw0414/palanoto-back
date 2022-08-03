package com.joje.palanoto.repository.account;

import com.joje.palanoto.model.entity.account.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}