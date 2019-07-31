package com.ogic.spikesystemorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author ogic
 */
@EnableEurekaClient
@SpringBootApplication
public class SpikeSystemOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemOrderServiceApplication.class, args);
    }

}
