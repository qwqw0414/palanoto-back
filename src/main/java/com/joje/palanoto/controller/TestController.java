package com.joje.palanoto.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/palanoto")
public class TestController {

    @GetMapping(value = "/test/public")
    public String testPublic() throws Exception {
        return "public";
    }

    @GetMapping(value = "/test/user")
    @PreAuthorize("hasAnyRole('USER')")
    public String testUser() throws Exception {
        return "user";
    }

    @GetMapping(value = "/test/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String testAdmin() throws Exception {
        return "admin";
    }

    @GetMapping(value = "/user/test")
    public String userTest() throws Exception {
        return "user";
    }

    @GetMapping(value = "/admin/test")
    public String adminTest() throws Exception {
        return "admin";
    }
}
