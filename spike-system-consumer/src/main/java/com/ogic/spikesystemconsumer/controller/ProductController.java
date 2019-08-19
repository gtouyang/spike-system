package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.service.ProductExposeService;
import com.ogic.spikesystemconsumer.entity.ProductDisplayEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    ProductExposeService productExposeService;

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable("id") long id, Model model) {
        Optional<ProductEntity> product = productExposeService.getProductById(id);
        if (product.isPresent()) {
            ProductEntity entity = product.get();
            ProductDisplayEntity display = new ProductDisplayEntity();
            Date current = new Date();
            display.setName(entity.getName());
            display.setInfo(entity.getInfo());
            display.setPrice(entity.getOriginPrice().toString());
            display.setTimeInfo("目前还没有活动");
            if (current.before(entity.getSpikeStartTime())){
                display.setTimeInfo("秒杀开始： " + entity.getSpikeStartTime());
            }else if (current.before(entity.getSpikeEndTime())){
                display.setTimeInfo("秒杀结束： " + entity.getSpikeEndTime());
                display.setPrice(entity.getSpikePrice().toString());
            }
            model.addAttribute("product", product.get());
            model.addAttribute("display", display);
        }
        return "show_goods";
    }
}
