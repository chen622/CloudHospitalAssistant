package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Registration;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface RegistrationMapper extends MyMapper<Registration> {
     List<Registration> getAllWaitingRegistration();
      List<Registration> getRegistrationByPatientName(String name);
}