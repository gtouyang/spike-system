package com.ogic.spikesystemshopservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ogic
 */
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.ogic.spikesystemapi.service")
@SpringBootApplication
public class SpikeSystemShopServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemShopServiceApplication.class, args);
    }

}
