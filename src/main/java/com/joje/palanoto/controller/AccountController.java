package com.joje.palanoto.controller;

import com.joje.palanoto.model.dto.account.SignupDto;
import com.joje.palanoto.model.dto.account.UserDto;
import com.joje.palanoto.model.vo.ResultVo;
import com.joje.palanoto.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/palanoto/account")
public class AccountController {

    private final AccountService accountService;

    /**
     * 아이디 중복 체크
     */
    @GetMapping(value = "/duple/{userId}", produces = "application/json; charset=utf8")
    public ResponseEntity<ResultVo> idDuplicateCheck(@PathVariable(value = "userId") String userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.put("isValid", accountService.userIdDuplicateCheck(userId));
        return new ResponseEntity<>(resultVo, HttpStatus.OK);
    }

    /**
     * 회원 가입
     */
    @PostMapping(value = "/signup", produces = "application/json; charset=utf8")
    public ResponseEntity<ResultVo> register(@Validated @RequestBody SignupDto param) throws Exception {

        UserDto userDto = accountService.signup(param);

        ResultVo resultVo = new ResultVo();
        resultVo.put("user", userDto);

        return new ResponseEntity<>(resultVo, HttpStatus.OK);
    }
}
