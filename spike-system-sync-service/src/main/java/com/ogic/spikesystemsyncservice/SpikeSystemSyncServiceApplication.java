package com.ogic.spikesystemsyncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author ogic
 */
@EnableEurekaClient
@SpringBootApplication
public class SpikeSystemSyncServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemSyncServiceApplication.class, args);
    }

}
