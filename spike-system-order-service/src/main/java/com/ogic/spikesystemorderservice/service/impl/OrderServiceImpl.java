package com.ogic.spikesystemorderservice.service.impl;

import com.ogic.spikesystemapi.entity.GoodEntity;
import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemapi.service.GoodExposeService;
import com.ogic.spikesystemorderservice.component.GuavaCache;
import com.ogic.spikesystemorderservice.mapper.AmountMapper;
import com.ogic.spikesystemorderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * @author ogic
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final String DEFAULT_ORDER_HEAD = "ORDER";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

}
