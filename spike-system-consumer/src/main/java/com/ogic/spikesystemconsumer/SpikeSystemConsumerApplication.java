package com.ogic.spikesystemconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ogic
 */
@EnableFeignClients(basePackages = "com.ogic.spikesystemapi.service")
@EnableEurekaClient
@SpringBootApplication
public class SpikeSystemConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemConsumerApplication.class, args);
    }

}
