package com.joje.palanoto.service.impl;

import com.joje.palanoto.common.security.JwtTokenProvider;
import com.joje.palanoto.model.dto.account.LoginDto;
import com.joje.palanoto.model.dto.account.TokenResponseDto;
import com.joje.palanoto.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service("AuthService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public TokenResponseDto login(LoginDto param) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(param.getUserId(), param.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return new TokenResponseDto(param.getUserId(), tokenProvider.createToken(authentication));
    }

}
