package cn.neuedu.his.service.impl;

import cn.neuedu.his.service.PatientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceImplTest {

    @Autowired
    private PatientService patientService;

    @Test
    public void findPatientAndPaymentInfo() {
    }

    @Test
    public void findPatientAndNotConsumePayment() {
    }

//    @Test
//    public void findPatientAndNotTakeDrug() {
//        patientService.findPatientAndNotTakeDrug(1);
//    }
//
//    @Test
//    public void findPatientAndDrugDuringDate() {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date start = new Date();
//        Date end = new Date();
//        try {
//            start = formatter.parse("2019-05-29 06:00:00");
//            end = formatter.parse("2019-05-30 12:00:00");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        patientService.findPatientAndDrugDuringDate(1, start, end);
//    }
}