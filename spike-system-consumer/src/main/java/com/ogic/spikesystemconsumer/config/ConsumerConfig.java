package com.ogic.spikesystemconsumer.config;

import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ogic
 */
@Configuration
public class ConsumerConfig {
    @Bean
    public TokenVerifyUtil getTokenVerifyUtil(@Value("token.secret") String secret){
        return new TokenVerifyUtil(secret);
    }
}
