package com.ogic.spikesystemorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author ogic
 */
@EnableAsync
@EnableFeignClients(basePackages = "com.ogic.spikesystemapi.service")
@EnableEurekaClient
@SpringBootApplication
public class SpikeSystemOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemOrderServiceApplication.class, args);
    }

}
