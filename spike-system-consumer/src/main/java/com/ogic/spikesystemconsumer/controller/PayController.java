package com.ogic.spikesystemconsumer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ogic
 */
@Controller
public class PayController {

    @PostMapping("/pay")
    public String payOrder(HttpServletRequest request, @RequestParam final String payPassword){
        return "show_orders";
    }
}
