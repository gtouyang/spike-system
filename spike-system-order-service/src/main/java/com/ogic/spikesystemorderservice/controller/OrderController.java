package com.ogic.spikesystemorderservice.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemorderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    TokenVerifyUtil tokenVerifyUtil;

    @PostMapping("/order")
    public Optional<OrderEntity> order(@RequestParam Long productId, @RequestParam Integer amount, HttpServletRequest request){

        String username = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                                        .map(str -> str.substring(1, str.length()-1))
                                        .map(t -> tokenVerifyUtil.decodeToken(t))
                                        .map(jwt -> tokenVerifyUtil.verifyToken(jwt))
                                        .map(jwt -> jwt.getClaim("username").asString())
                                        .orElse(null);
        if (username != null){
            return Optional.ofNullable(orderService.order(productId, username, amount));
        }
        return Optional.empty();
    }

}
