package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemapi.service.AuthExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpSession;
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
    public RedirectView getToken(@RequestParam String username, @RequestParam String password, HttpSession session){
        Optional<String> token = authExposeService.login(username, password);
        if (token.isPresent()) {
            session.setAttribute(USERNAME_KEY, username);
            session.setAttribute(TOKEN_KEY, token.get());
            return new RedirectView("/index");
        }
        return new RedirectView("login");
    }

    @GetMapping("/logout")
    public RedirectView removeToken(HttpSession session){
        session.removeAttribute(USERNAME_KEY);
        session.removeAttribute(TOKEN_KEY);
        return new RedirectView("/login");
    }

    @PostMapping("/register")
    public RedirectView register(@RequestParam String username, @RequestParam final String password, HttpSession session){
        Optional<String> result = authExposeService.register(new UserEntity()
                                                        .setUsername(username)
                                                        .setPassword(password));
        if (result.isPresent()){
            Optional<String> token = authExposeService.login(username, password);
            if (token.isPresent()){
                session.setAttribute(USERNAME_KEY, username);
                session.setAttribute(TOKEN_KEY, token.get());
                return new RedirectView("/index");
            }
        }
        return new RedirectView("/login");
    }

}
