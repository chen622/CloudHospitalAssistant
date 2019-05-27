package cn.neuedu.his.service;
import cn.neuedu.his.mapper.RegistrationMapper;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.util.inter.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface RegistrationService extends Service<Registration> {

     List<Registration> getAllWaitingRegistration(Integer doctorID);
      List<Registration> getRegistrationByPatientName(String name,Integer doctorID);

}
