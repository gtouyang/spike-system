package com.ogic.spikesystemauthenticationservice;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.internal.cglib.core.$ObjectSwitchCallback;
import com.ogic.spikesystemapi.entity.UserEntity;
import com.ogic.spikesystemauthenticationservice.component.TokenCreateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemAuthenticationServiceApplicationTests {

    @Autowired
    TokenCreateUtil tokenCreateUtil;

    @Test
    public void contextLoads() {
        UserEntity userEntity = new UserEntity().setUsername("test");
        Long current = System.currentTimeMillis();
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcGlrZSIsImV4cCI6MTU2NjQzODMxNiwidXNlcm5hbWUiOiJvZ2ljIn0.w1ZnNaN-ecaozAYV44Ar4cFiGF0Co-BNBgduDJU0-7gNf-KD8lWTRmF3zUW7h9XFUc-EYGEADBVWRErXwwQZtQ";
        DecodedJWT jwt = tokenCreateUtil.decodeToken(token);
        jwt =tokenCreateUtil.verifyToken(jwt);
        System.out.println(jwt);
        Date date = new Date();
    }

}
