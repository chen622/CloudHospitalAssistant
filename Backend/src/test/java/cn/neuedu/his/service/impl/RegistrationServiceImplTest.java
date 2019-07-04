package cn.neuedu.his.service.impl;

import cn.neuedu.his.service.RegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationServiceImplTest {
    @Autowired
    RegistrationService registrationService;

    @Test
    public void setRegistrationSequence() {
        registrationService.setRegistrationSequence();
    }

    @Test
    public void setRegistrationSequenceToday() {
        registrationService.setRegistrationSequenceToday();
    }
}