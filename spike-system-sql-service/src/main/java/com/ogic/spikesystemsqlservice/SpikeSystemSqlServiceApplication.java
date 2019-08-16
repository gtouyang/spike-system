package com.ogic.spikesystemsqlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SpikeSystemSqlServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemSqlServiceApplication.class, args);
    }

}
