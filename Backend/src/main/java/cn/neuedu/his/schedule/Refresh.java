package cn.neuedu.his.schedule;

import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Refresh {
    @Autowired
    ConstantVariableService constantVariableService;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    DoctorService doctorService;

//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void refreshConstant() {
        registrationService.setRegistrationSequence();
        doctorService.clearTemporaryMedical();
    }
}
