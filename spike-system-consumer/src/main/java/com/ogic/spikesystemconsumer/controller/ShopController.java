package com.ogic.spikesystemconsumer.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.entity.ShopEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemapi.service.ShopExposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@Controller
public class ShopController {

    @Autowired
    ShopExposeService shopExposeService;

    @Autowired
    GoodExposeService goodExposeService;

    @Autowired
    TokenVerifyUtil tokenVerifyUtil;

    @GetMapping("/store-seller/{shopId}")
    public String storeSellerView(@PathVariable("shopId") long shopId,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  Model model){
        Optional<ShopEntity> shopOptional = shopExposeService.getShopById(shopId);
        if (!shopOptional.isPresent() || shopOptional.get().getOwner() == null){
            return null;
        }
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie cookie : cookies){
            if ("token".equals(cookie.getName())){
                token=cookie.getValue();
            }
        }
        String username = null;
        if (token != null){
            DecodedJWT jwt = tokenVerifyUtil.decodeToken(token);
            if (jwt != null){
                jwt = tokenVerifyUtil.verifyToken(jwt);
                if (jwt != null){
                    username = jwt.getClaim("username").asString();
                }
            }
        }

        if (shopOptional.get().getOwner().equals(username)){
            Cookie cookie = new Cookie("shopId", Long.toString(shopId));
            response.addCookie(cookie);
            Optional<List<GoodEntity>> goodListOptional = goodExposeService.getGoodsByShopId(shopId);
            model.addAttribute("shop", shopOptional.get());
            goodListOptional.ifPresent(goodEntities -> model.addAttribute("goods", goodEntities));
            return "store_seller";
        }
        return "redirect:login";
    }
}
