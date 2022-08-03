package com.joje.palanoto.model.dto.account;

import com.joje.palanoto.common.constants.RoleType;
import com.joje.palanoto.model.entity.account.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userNo;
    private String userId;
    private String userName;
    private String regDate;
    private List<RoleType> roles;

    public void setRoles(List<RoleEntity> roles) {
        this.roles = new ArrayList<>();
        for (RoleEntity role : roles) {
            this.roles.add(role.getRoleName());
        }
    }

}
