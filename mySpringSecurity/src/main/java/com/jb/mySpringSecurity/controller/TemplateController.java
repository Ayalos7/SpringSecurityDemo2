package com.jb.mySpringSecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {
    @GetMapping("login")
    public String getLoginView(){
        return "login"; //same as the name in the template folder - template/login
    }
    @GetMapping("mainpage")
    public String getMainPageView(){
        return "mainpage";
    }
}
