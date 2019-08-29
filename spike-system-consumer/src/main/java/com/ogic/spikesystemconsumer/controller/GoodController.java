package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import com.ogic.spikesystemconsumer.entity.GoodDisplayEntity;
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
public class GoodController {

    @Autowired
    GoodExposeService goodExposeService;

    @Autowired
    ShopExposeService shopExposeService;

    @GetMapping("/good/{id}")
    public String getGoodById(@PathVariable("id") long id, Model model) {
        Optional<GoodEntity> goodOptional = goodExposeService.getGoodById(id);

        if (goodOptional.isPresent()) {

            GoodEntity good = goodOptional.get();

            Optional<ShopEntity> shopOptional = shopExposeService.getShopById(good.getShopId());

            GoodDisplayEntity display = new GoodDisplayEntity();
            Date current = new Date();
            display.setName(good.getName());
            display.setInfo(good.getInfo());
            display.setPrice(good.getOriginPrice().toString());
            display.setTimeInfo("目前还没有活动");
            if (good.getSpikeStartTime() != null && current.before(good.getSpikeStartTime())) {
                display.setTimeInfo("秒杀开始： " + good.getSpikeStartTime());
            } else if (good.getSpikeEndTime() != null && current.before(good.getSpikeEndTime())) {
                display.setTimeInfo("秒杀结束： " + good.getSpikeEndTime());
                display.setPrice(good.getSpikePrice().toString());
            }
            shopOptional.ifPresent(shopEntity -> model.addAttribute("shop", shopEntity));
            model.addAttribute("good", good);
            model.addAttribute("display", display);
        }
        return "show_goods";
    }
}
