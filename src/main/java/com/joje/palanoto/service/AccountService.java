package com.joje.palanoto.service;

import com.joje.palanoto.model.dto.account.SignupDto;
import com.joje.palanoto.model.dto.account.UserDto;

public interface AccountService {

    boolean userIdDuplicateCheck(String userId);

    UserDto signup(SignupDto param);

    UserDto getUser(String userId);
}
