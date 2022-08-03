package com.joje.palanoto.service.impl;

import com.joje.palanoto.common.constants.RoleType;
import com.joje.palanoto.model.dto.account.SignupDto;
import com.joje.palanoto.model.dto.account.UserDto;
import com.joje.palanoto.model.entity.account.RoleEntity;
import com.joje.palanoto.model.entity.account.UserEntity;
import com.joje.palanoto.repository.account.UserRepository;
import com.joje.palanoto.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("AccountService")
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public boolean userIdDuplicateCheck(String userId) {
        return userRepository.countByUserId(userId) == 0;
    }


    @Override
    @Transactional
    public UserDto signup(SignupDto param) {

        if(userRepository.countByUserId(param.getUserId()) > 0)
            throw new RuntimeException("이미 가입된 유저입니다.");

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(RoleType.ROLE_USER));

        UserEntity user = UserEntity.builder()
                                    .userId(param.getUserId())
                                    .password(passwordEncoder.encode(param.getPassword()))
                                    .userName(param.getUserName())
                                    .regDate(LocalDateTime.now())
                                    .roles(roles)
                                    .build();

        user = userRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUser(String userId) {
        return modelMapper.map(userRepository.findByUserId(userId).get(), UserDto.class);
    }
}

