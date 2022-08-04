package com.joje.palanoto.common.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    ROLE_ROOT(1),
    ROLE_ADMIN(2),
    ROLE_USER(3);

    private final long roleId;
}
