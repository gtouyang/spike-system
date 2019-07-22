package com.ogic.spikesystemconsumer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("order/test")
    public String orderTest(){
        return "buyTest";
    }
}
