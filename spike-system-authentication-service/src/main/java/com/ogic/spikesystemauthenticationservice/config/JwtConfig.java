package com.ogic.spikesystemauthenticationservice.config;

import com.ogic.spikesystemauthenticationservice.common.TokenCreateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ogic
 */
@Configuration
public class JwtConfig {

    @Bean
    public TokenCreateUtil getTokenCreateUtil(
            @Value("jwt.secret")
            String secret){
        return new TokenCreateUtil(secret);
    }
}
