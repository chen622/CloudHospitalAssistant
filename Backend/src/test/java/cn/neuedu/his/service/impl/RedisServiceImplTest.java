package cn.neuedu.his.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceImplTest {

    @Autowired
    RedisServiceImpl redisService;

    @Test
    public void setInvoiceSerialsNumberList() {
        redisService.setInvoiceSerialsNumberList(50, 60);
    }

    @Test
    public void getInvoiceSerialsNumberFromFront() {
    }

    @Test
    public void setRegistrationSequenceList() {
        redisService.setRegistrationSequenceList(2, 5);
    }

    @Test
    public void getRegistrationSequenceFromFront() {
    }

    @Test
    public void addRegistrationSequenceList() {
    }

    @Test
    public void getMapAll() throws Exception {
        Map<String, Integer> map = redisService.getMapAll("userType");
        Set<String> set = map.keySet();
    }

    @Test
    public void getTemporaryMedicalRecord() {
        try {
            System.out.println(redisService.getTemporaryMedicalRecord(11111111));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}