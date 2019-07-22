package com.ogic.spikesystemapi;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ogic.spikesystemapi.component.TokenTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemApiApplicationTests {

    @Autowired
    TokenTemplate tokenChecker;

    @Test
    public void contextLoads() {
        tokenChecker.updateAlgorithmByHMAC256("secret");
        String token = tokenChecker.createToken("test", 60L);
        System.out.println(token);
        if (token != null) {
            token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZXN0IiwiZXhwIjoxNTYzNjI1OTExfQ.1X47h2Nt4kDMvaejxydj9EpEhiTDPR0CwNmK3HOdp68";
            DecodedJWT jwt = tokenChecker.vefifyToken(token, "test");
            if (jwt != null) {
                System.out.println(jwt.getHeader());
                System.out.println(jwt.getPayload());
                System.out.println(jwt.getSignature());
            }
        }
    }

}
