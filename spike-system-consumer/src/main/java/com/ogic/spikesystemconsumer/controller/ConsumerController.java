package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.service.OrderExposeService;
import com.ogic.spikesystemapi.service.ProductExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * @author ogic
 */
@Controller
public class ConsumerController {
//    private static final String PRODUCT_SERVICE_REST_URL_PREFIX = "http://SPIKE-SYSTEM-PRODUCT-SERVICE";

//    private static final String ORDER_SERVICE_REST_URL_PREFIX = "http://SPIKE-SYSTEM-ORDER-SERVICE";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProductExposeService productExposeService;

    @Autowired
    OrderExposeService orderExposeService;

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable("id") long id, Model model) {
        ProductEntity product = productExposeService.getProductById(id);
        model.addAttribute("product", product);
        return "show_goods";
    }

    @ResponseBody
    @PostMapping("/buy")
    public OrderEntity buy(OrderEntity order){
        return orderExposeService.buy(order);
    }
}
