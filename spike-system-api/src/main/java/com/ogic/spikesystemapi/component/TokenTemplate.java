package com.ogic.spikesystemapi.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * token创建与验证工具
 *
 * @author ogic
 * @date 2019-07-19
 */
@Component
public class TokenTemplate {

    private Algorithm algorithm;

    public void updateAlgorithmByHMAC256(String originValue) {
        algorithm = Algorithm.HMAC256(originValue);
    }

    public String createToken(String msg, Long lifetime) {
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer(msg)
                    .withExpiresAt(new Date(System.currentTimeMillis() + lifetime))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
        }
        return token;
    }

    public DecodedJWT vefifyToken(String token, String msg) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(msg)
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
        }
        return jwt;
    }

    public DecodedJWT decodeToken(String token) {
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception) {

        }
        return jwt;
    }

}
