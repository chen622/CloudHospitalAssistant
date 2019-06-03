package cn.neuedu.his.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceImplTest {

    @Autowired
    RedisServiceImpl redisService;

    @Test
    public void setInvoiceSerialsNumberList() {
    }

    @Test
    public void getInvoiceSerialsNumberFromFront() {
    }

    @Test
    public void setRegistrationSequenceList() {
        redisService.setRegistrationSequenceList(1, 3);
    }

    @Test
    public void getRegistrationSequenceFromFront() {
    }

    @Test
    public void addRegistrationSequenceList() {
    }
}