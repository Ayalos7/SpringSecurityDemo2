package com.jb.mySpringSecurity.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TestController {

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showTest(){
        return "working";
    }
}
