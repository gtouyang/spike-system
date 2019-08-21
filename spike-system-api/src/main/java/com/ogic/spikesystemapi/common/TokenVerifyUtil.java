package com.ogic.spikesystemapi.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author ogic
 */
public class TokenVerifyUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Algorithm algorithm;

    public TokenVerifyUtil(Algorithm algorithm){
        this.algorithm = algorithm;
    }

    public TokenVerifyUtil(String secret){
        this(Algorithm.HMAC512(secret));
    }

    public void updateAlgorithm(final String secret){
        algorithm = Algorithm.HMAC512(secret);
    }

    public DecodedJWT verifyToken(DecodedJWT decodedJWT) {
        DecodedJWT verifyJWT;
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(decodedJWT.getIssuer())
                    .withClaim("username", decodedJWT.getClaim("username").asString())
                    .build(); //Reusable verifier instance
            verifyJWT = verifier.verify(decodedJWT.getToken());
        } catch (JWTVerificationException exception) {
            verifyJWT = null;
            logger.info("token is disabled");
        }
        return verifyJWT;
    }
    public DecodedJWT decodeToken(String token) {
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception) {
            jwt = null;
            logger.info("fail to decode token");
        }
        return jwt;
    }
}
