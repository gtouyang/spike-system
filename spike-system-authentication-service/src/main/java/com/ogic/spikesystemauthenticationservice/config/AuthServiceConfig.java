package com.ogic.spikesystemauthenticationservice.config;

import com.ogic.spikesystemauthenticationservice.component.TokenCreateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ogic
 */
@Configuration
public class AuthServiceConfig {

    @Bean
    public TokenCreateUtil getTokenCreateUtil(@Value("token.secret") String secret){
        return new TokenCreateUtil(secret);
    }
}
