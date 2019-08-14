package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.ProductEntity;
import com.ogic.spikesystemapi.service.ProductExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    ProductExposeService productExposeService;

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable("id") long id, Model model) {
        Optional<ProductEntity> product = productExposeService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
        }
        return "show_goods";
    }
}
