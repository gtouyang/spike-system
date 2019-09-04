package com.ogic.spikesystemgoodservice.controller;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemgoodservice.service.GoodService;
import com.ogic.spikesystemgoodservice.service.impl.GoodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class GoodController {

    @Autowired
    GoodService goodService;

    @GetMapping("/good")
    public Optional<GoodEntity> getGoodById(@RequestParam long id) {
        return goodService.getGoodById(id);
    }

    @PostMapping("/good")
    public Optional<Integer> addGood(@RequestBody GoodEntity good) {
        return goodService.addGood(good);
    }
}
