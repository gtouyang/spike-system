package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.service.OrderExposeService;
import com.ogic.spikesystemapi.service.ProductExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
/**
 * @author ogic
 */
@Controller
public class OrderController {

    @Autowired
    OrderExposeService orderExposeService;

    @Autowired
    ProductExposeService productExposeService;

    private final String TOKEN_KEY = "token";

    @PostMapping("/order")
    public String order(@RequestParam long productId, @RequestParam Integer amount, HttpServletRequest request, Model model){

        Optional<ProductEntity> productOptional = productExposeService.getProductById(productId);
        Cookie[] cookies = request.getCookies();
        Optional<String> tokenOptional = Optional.empty();
        for (Cookie cookie:cookies) {
            if (TOKEN_KEY.equals(cookie.getName())) {
                tokenOptional = Optional.ofNullable(cookie.getValue())
                                        .map(String::valueOf);
            }
        }
        if (productOptional.isPresent() && tokenOptional.isPresent()) {
            Optional<OrderEntity> order = orderExposeService.order(productOptional.get().getId(), amount, tokenOptional.get());
            if (order.isPresent()){
                model.addAttribute("product", productOptional.get());
                model.addAttribute("order", order.get());
                return "place_order";
            }
        }
        return "redirect:product/"+productId;
    }

}
