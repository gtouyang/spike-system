package com.ogic.spikesystempayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author ogic
 */
@EnableAsync
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.ogic.spikesystemapi.service")
@SpringBootApplication
public class SpikeSystemPayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemPayServiceApplication.class, args);
    }

}
