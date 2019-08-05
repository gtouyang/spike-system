package com.ogic.spikesystemauthenticationservice.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.common.TokenVerifyUtil;
import com.ogic.spikesystemapi.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * @author ogic
 */
public class TokenCreateUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Algorithm algorithm;

    private TokenVerifyUtil tokenVerifyUtil;

    @Value("token.secret")
    private String secret;

    public TokenCreateUtil(){
        algorithm = Algorithm.HMAC512(secret);
        tokenVerifyUtil = new TokenVerifyUtil(algorithm);
    }

    public TokenCreateUtil(Algorithm algorithm){
        this.algorithm = algorithm;
        tokenVerifyUtil = new TokenVerifyUtil(algorithm);
    }

    public TokenCreateUtil(final String secret){
        this(Algorithm.HMAC512(secret));
    }

    public void updateAlgorithm(final String secret){
        algorithm = Algorithm.HMAC512(secret);
    }

    public DecodedJWT verifyToken(String token, UserDetails userDetails) {
        return tokenVerifyUtil.verifyToken(token, userDetails);
    }

    public DecodedJWT decodeToken(String token) {
        return tokenVerifyUtil.decodeToken(token);
    }

    public String createToken(UserEntity userEntity, Long lifetime) {
        String token;
        try {
            token = JWT.create()
                    .withClaim("username", userEntity.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + lifetime))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            token = null;
            logger.info("create token failed");
        }
        return token;
    }
}
