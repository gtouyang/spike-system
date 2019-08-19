package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.service.OrderExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;
/**
 * @author ogic
 */
@Controller
public class OrderController {

    @Autowired
    OrderExposeService orderExposeService;

    private final String TOKEN_KEY = "token";

    @PostMapping("/order")
    public String order(@RequestParam ProductEntity product, @RequestParam Integer amount, HttpSession session, Model model){

        Optional<String> token = Optional.ofNullable(session.getAttribute(TOKEN_KEY).toString());
        if (token.isPresent()) {
            Optional<OrderEntity> order = orderExposeService.order(product.getId(), amount, token.get());
            if (order.isPresent()){
                model.addAttribute("product", product);
                model.addAttribute("order", order.get());
                return "place_order";
            }
        }
        return "redirect:product/"+product.getId();
    }

}
