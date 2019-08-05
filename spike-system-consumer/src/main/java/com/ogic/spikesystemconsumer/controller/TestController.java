package com.ogic.spikesystemconsumer.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @GetMapping("/token/test")
    @ResponseBody
    public String tokenTest(HttpServletRequest request){
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
