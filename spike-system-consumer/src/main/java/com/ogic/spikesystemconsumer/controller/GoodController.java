package com.ogic.spikesystemconsumer.controller;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import com.ogic.spikesystemconsumer.entity.GoodDisplayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
            display.setPrice(Double.toString(good.getOriginPrice()));
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

    @GetMapping("/add-good")
    public String addGoodView(){
        return "release_goods";
    }

    @PostMapping("/add-good")
    public String addGood(@RequestParam String goodName,
                          @RequestParam String goodInfo,
                          @RequestParam String originPrice,
                          @RequestParam String spikePrice,
                          @RequestParam String spikeTime,
                          @RequestParam String goodAmount,
                          HttpServletRequest request){
        GoodEntity good = new GoodEntity()
                .setName(goodName)
                .setInfo(goodInfo);
        if (goodAmount != null){
            good.setAmount(Integer.parseInt(goodAmount));
        }
        if (originPrice != null){
            good.setOriginPrice(Double.parseDouble(originPrice));
        }
        if (spikePrice != null){
            good.setSpikePrice(Double.parseDouble(spikePrice));
        }
        if (spikeTime != null){
            String[] times = spikeTime.split(" - ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m aa", Locale.ENGLISH);
            try {
                good.setSpikeStartTime(dateFormat.parse(times[0]));
                good.setSpikeEndTime(dateFormat.parse(times[1]));
            }catch (ParseException e){
                good.setSpikeStartTime(null);
                good.setSpikeEndTime(null);
                logger.error(e.getMessage());
            }
        }
        return "redirect:store-seller";
    }
}
