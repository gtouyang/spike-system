package com.ogic.spikesystemconsumer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ConsumerController {
    private final String USERNAME_KEY = "username";
    @GetMapping("/index")
    public String index(HttpSession session){
        if (session.getAttribute(USERNAME_KEY) == null) {
            session.setAttribute(USERNAME_KEY, "未登录");
        }
        return "index";
    }
}
