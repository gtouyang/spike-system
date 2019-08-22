package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemapi.service.AuthExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author ogic
 */
@Controller
public class AuthController {

    private final String USERNAME_KEY = "username";
    private final String TOKEN_KEY = "token";

    @Autowired
    AuthExposeService authExposeService;

    @Autowired
    TokenVerifyUtil tokenVerifyUtil;



    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam final String username, @RequestParam final String password, @RequestParam String verificationCode, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String correctVerificationCode = null;
        for (Cookie cookie : cookies) {
            if ("verificationCode".equals(cookie.getName())) {
                correctVerificationCode = cookie.getValue();
                break;
            }
        }
        if (correctVerificationCode != null && correctVerificationCode.equals(verificationCode)) {
            return login(username, password, response);
        }
        return new RedirectView("/login");
    }

    public RedirectView login(final String username, final String password, HttpServletResponse response) {
        Optional<String> token = authExposeService.login(username, password);

        if (token.isPresent()) {
            Cookie usernameCookie = new Cookie(USERNAME_KEY, username);
            usernameCookie.setHttpOnly(true);
            usernameCookie.setSecure(true);
            Cookie tokenCookie = new Cookie(TOKEN_KEY, token.get().replaceAll("\"", ""));
            tokenCookie.setHttpOnly(true);
            tokenCookie.setSecure(true);
            response.addCookie(usernameCookie);
            response.addCookie(tokenCookie);
            return new RedirectView("/index");
        }
        return new RedirectView("login");
    }

    @PostMapping("/register")
    public RedirectView register(@RequestParam final String username, @RequestParam final String password, HttpServletResponse response){
        Optional<String> result = authExposeService.register(new UserEntity()
                                                        .setUsername(username)
                                                        .setPassword(password));
        if (result.isPresent()){
            return login(username, password, response);
        }
        return new RedirectView("/login");
    }

}
