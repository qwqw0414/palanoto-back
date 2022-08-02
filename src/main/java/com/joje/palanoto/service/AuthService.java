package com.joje.palanoto.service;

import com.joje.palanoto.model.dto.account.LoginDto;
import com.joje.palanoto.model.dto.account.TokenResponseDto;

public interface AuthService {
    TokenResponseDto login(LoginDto param);
}
