package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Registration;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component

public interface RegistrationMapper extends MyMapper<Registration> {
     List<Registration> getAllWaitingRegistration(Integer doctorID);
     List<Registration> getRegistrationByPatientName(String name,Integer doctorID,Integer state);
     ArrayList<Registration> getAllRegistrationWaitingByPatientId(@Param("patientId") Integer patientId, @Param("state1") Integer state1, @Param("state2") Integer state2);
}