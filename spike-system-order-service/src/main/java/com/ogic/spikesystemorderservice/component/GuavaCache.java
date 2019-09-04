package com.ogic.spikesystemorderservice.component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 用于缓存已经售罄的商品
 * @author ogic
 */
@Component
public class GuavaCache {
    private Cache<Long, Long> disableGood;

    public GuavaCache(){
        disableGood = CacheBuilder
                .newBuilder()
                .maximumSize(100)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    void add(Long goodId){
        disableGood.put(goodId, System.currentTimeMillis());
    }

    boolean contain(Long goodId){
        return disableGood.getIfPresent(goodId) != null;
    }

    void remove(Long goodId){
        disableGood.invalidate(goodId);
    }
}
