package com.ogic.spikesystemproductservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients(basePackages = "com.ogic.spikesystemapi.service")
@SpringBootApplication
public class SpikeSystemProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemProductServiceApplication.class, args);
    }

}
