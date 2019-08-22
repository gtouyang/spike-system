package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.service.ProductExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import com.ogic.spikesystemconsumer.entity.ProductDisplayEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.Optional;

/**
 * @author ogic
 */
@Controller
public class ProductController {

    @Autowired
    ProductExposeService productExposeService;

    @Autowired
    ShopExposeService shopExposeService;

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable("id") long id, Model model) {
        Optional<ProductEntity> productOptional = productExposeService.getProductById(id);

        if (productOptional.isPresent()) {

            ProductEntity product = productOptional.get();

            Optional<ShopEntity> shopOptional = shopExposeService.getShopById(product.getShopId());

            ProductDisplayEntity display = new ProductDisplayEntity();
            Date current = new Date();
            display.setName(product.getName());
            display.setInfo(product.getInfo());
            display.setPrice(product.getOriginPrice().toString());
            display.setTimeInfo("目前还没有活动");
            if (product.getSpikeStartTime() != null && current.before(product.getSpikeStartTime())) {
                display.setTimeInfo("秒杀开始： " + product.getSpikeStartTime());
            } else if (product.getSpikeEndTime() != null && current.before(product.getSpikeEndTime())) {
                display.setTimeInfo("秒杀结束： " + product.getSpikeEndTime());
                display.setPrice(product.getSpikePrice().toString());
            }
            shopOptional.ifPresent(shopEntity -> model.addAttribute("shop", shopEntity));
            model.addAttribute("product", product);
            model.addAttribute("display", display);
        }
        return "show_goods";
    }
}
