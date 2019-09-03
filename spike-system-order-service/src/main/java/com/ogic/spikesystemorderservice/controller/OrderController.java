package com.ogic.spikesystemorderservice.controller;

import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import com.ogic.spikesystemapi.entity.OrderEntity;
import com.ogic.spikesystemorderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author ogic
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    TokenVerifyUtil tokenVerifyUtil;


}
