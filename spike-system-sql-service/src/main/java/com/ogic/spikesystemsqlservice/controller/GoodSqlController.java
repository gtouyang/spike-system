package com.ogic.spikesystemsqlservice.controller;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemsqlservice.mapper.GoodMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class GoodSqlController {

    @Resource
    GoodMapper goodMapper;

    @GetMapping(value = "/sql/select/good/{id}")
    public Optional<GoodEntity> getGoodById(@PathVariable long id) {
        return Optional.ofNullable(goodMapper.getGoodById(id));
    }

    @GetMapping(value = "/sql/select/goods")
    public Optional<List<GoodEntity>> getGoods(@RequestParam long offest, @RequestParam Integer rows) {
        return Optional.ofNullable(goodMapper.getGoods(offest, rows));
    }

    @PostMapping(value = "/sql/insert/good")
    public Optional<Integer> insertGood(@RequestBody GoodEntity good) {
        return Optional.ofNullable(goodMapper.insertGood(good));
    }
}
