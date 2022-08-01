package com.joje.palanoto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/palanoto/test")
public class TestController {

    @GetMapping(value = "/public")
    public String testPublic() throws Exception {
        return "public";
    }

    @GetMapping(value = "/user")
    public String testUser() throws Exception {
        return "user";
    }

    @GetMapping(value = "/admin")
    public String testAdmin() throws Exception {
        return "admin";
    }

}
