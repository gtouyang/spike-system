package com.ogic.spikesystemorderservice.config;

import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ogic
 */
@Configuration
public class OrderConfig {
    @Bean
    public TokenVerifyUtil getTokenVerifyUtil(@Value("token.secret") String secret){
        return new TokenVerifyUtil(secret);
    }
}
