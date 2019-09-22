package com.ogic.spikesystemconsumer.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemapi.service.OrderQueryExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

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
            //jwt = tokenVerifyUtil.verifyToken(jwt);
        }
        if (goodOptional.isPresent()
                && goodOptional.get().getAmount() >= amount
                && jwt != null
                && jwt.getClaim(USERNAME_KEY) != null) {
            OrderEntity order = new OrderEntity()
                    .setGoodId(goodId)
                    .setAmount(amount)
                    .setOrderUsername(jwt.getClaim(USERNAME_KEY).asString());
            kafkaTemplate.send("waitingOrder", order);
            model.addAttribute("good", goodOptional.get());
            model.addAttribute("order", order);
            Optional<ShopEntity> shopOptional = shopExposeService.getShopById(goodOptional.get().getShopId());
            shopOptional.ifPresent(shopEntity -> model.addAttribute("shop", shopEntity));
            return "place_order";
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
//            jwt = tokenVerifyUtil.verifyToken(jwt);
            if (jwt != null){
                Optional<List<OrderEntity>> orderListOptional
                        = orderQueryExposeService.getAllOrders(
                                jwt.getClaim(USERNAME_KEY).asString());
                if (orderListOptional.isPresent()){
                    List<OrderEntity> orderList = orderListOptional.get();
                    List<Map<String, String>> mapList = new ArrayList<>(orderList.size());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    for(OrderEntity order : orderList){
                        Map<String, String> map = new HashMap<>(8);
                        map.put("orderId", Long.toString(order.getId()));
                        map.put("orderTime", dateFormat.format(order.getOrderTime()));
                        Optional<GoodEntity> goodOptional = goodExposeService.getGoodById(order.getGoodId());
                        Optional<ShopEntity> shopOptional = shopExposeService.getShopById(goodOptional.get().getShopId());
                        map.put("shopName", shopOptional.get().getShopName());
                        map.put("seller", shopOptional.get().getOwner());
                        map.put("goodName", goodOptional.get().getName());
                        map.put("goodInfo", goodOptional.get().getInfo());
                        map.put("price", order.getPayMoney().toString());
                        map.put("amount", Integer.toString(order.getAmount()));
                        mapList.add(map);
                    }
                    model.addAttribute("mapList", mapList);
                }
            }
        }
        return "show_orders";
    }

    @GetMapping("/refreshOrder")
    public String refreshOrder(@RequestParam String orderId, Model model){
        long id = Long.parseLong(orderId);
        if (id != 0){
            Optional<OrderEntity> orderOptional = orderQueryExposeService.getOrder(id);
            if (orderOptional.isPresent()){
                model.addAttribute("order", orderOptional.get());
                Optional<GoodEntity> goodOptional = goodExposeService.getGoodById(orderOptional.get().getGoodId());
                goodOptional.ifPresent(goodEntity -> model.addAttribute("good", goodEntity));
                Optional<ShopEntity> shopOptional = shopExposeService.getShopById(goodOptional.get().getShopId());
                shopOptional.ifPresent(shopEntity -> model.addAttribute("shop", shopEntity));
                return "place_order";
            }
        }
        return null;
    }

}
