package com.joje.palanoto.service.impl;

import com.joje.palanoto.model.entity.RoleEntity;
import com.joje.palanoto.model.entity.UserEntity;
import com.joje.palanoto.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service("UserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserId(username)
                             .map(user -> createUserDetails(user))
                             .orElseThrow(() -> new UsernameNotFoundException(username + " 존재하지 않는 UserId 입니다."));
    }

    private UserDetails createUserDetails(UserEntity user) {
//      GrantedAuthority grantedAuthority =  new SimpleGrantedAuthority(user.getRoles().toString());
        List<String> roles = new ArrayList<>();
        for(RoleEntity role : user.getRoles()) {
            roles.add(role.getRoleName().toString());
        }

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.join(",", roles));
        return new org.springframework.security.core.userdetails.User(
//                user.getUserId(),
                user.getUserNo().toString(),
                user.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
