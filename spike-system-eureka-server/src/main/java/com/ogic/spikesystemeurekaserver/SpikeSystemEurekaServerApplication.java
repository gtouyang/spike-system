package com.ogic.spikesystemeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author ogic
 */
@EnableEurekaServer
@SpringBootApplication
public class SpikeSystemEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemEurekaServerApplication.class, args);
    }

}
