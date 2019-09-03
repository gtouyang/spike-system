package com.ogic.spikesystemorderservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpikeSystemOrderServiceApplicationTests {


    @Test
    public void contextLoads() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        System.out.println(dateFormat.format(new Date()));
    }

}
