package com.ogic.spikesystemconsumer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ogic
 */
@Controller
public class ConsumerController {
    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        return "index";
    }
}
