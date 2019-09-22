package com.ogic.spikesystemconsumer.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemapi.service.OrderQueryExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
/**
 * @author ogic
 */
@Controller
public class OrderController {


    @Autowired
    GoodExposeService goodExposeService;

    @Autowired
    ShopExposeService shopExposeService;

    @Autowired
    OrderQueryExposeService orderQueryExposeService;

    @Autowired
    TokenVerifyUtil tokenVerifyUtil;

    @Autowired
    KafkaTemplate<Object, Object> kafkaTemplate;

    private final String TOKEN_KEY = "token";

    private final String USERNAME_KEY = "username";

    @PostMapping("/order")
    public String order(@RequestParam long goodId, @RequestParam Integer amount, HttpServletRequest request, Model model) {

        Optional<GoodEntity> goodOptional = goodExposeService.getGoodById(goodId);
        Cookie[] cookies = request.getCookies();
        Optional<String> tokenOptional = Optional.empty();
        for (Cookie cookie:cookies) {
            if (TOKEN_KEY.equals(cookie.getName())) {
                tokenOptional = Optional.ofNullable(cookie.getValue())
                                        .map(String::valueOf);
            }
        }
        DecodedJWT jwt = null;
        if (tokenOptional.isPresent()){
            jwt = tokenVerifyUtil.decodeToken(tokenOptional.get());
            jwt = tokenVerifyUtil.verifyToken(jwt);
        }
        if (goodOptional.isPresent() && jwt != null && jwt.getClaim(USERNAME_KEY) != null) {
            OrderEntity order = new OrderEntity()
                    .setGoodId(goodId)
                    .setAmount(amount)
                    .setOrderUsername(jwt.getClaim(USERNAME_KEY).asString());
            kafkaTemplate.send("waitingOrder", order);
            return orderList(request, model);
        }
        return "redirect:good/" + goodId;
    }

    @GetMapping("/orders")
    public String orderList(HttpServletRequest request, Model model){
        Cookie[] cookies = request.getCookies();
        Optional<String> tokenOptional = Optional.empty();
        for (Cookie cookie:cookies) {
            if (TOKEN_KEY.equals(cookie.getName())) {
                tokenOptional = Optional.ofNullable(cookie.getValue())
                        .map(String::valueOf);
            }
        }
        if (tokenOptional.isPresent()) {
            DecodedJWT jwt = tokenVerifyUtil.decodeToken(tokenOptional.get());
            jwt = tokenVerifyUtil.verifyToken(jwt);
            if (jwt != null){
                Optional<List<OrderEntity>> orderListOptional
                        = orderQueryExposeService.getAllOrders(
                                jwt.getClaim(USERNAME_KEY).asString());
                if (orderListOptional.isPresent()){
                    List<OrderEntity> orderList = orderListOptional.get();
                    model.addAttribute("orders", orderList);
                }
            }
        }
        return "show_orders";
    }

}
