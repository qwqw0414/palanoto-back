package com.joje.palanoto.model.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

}
