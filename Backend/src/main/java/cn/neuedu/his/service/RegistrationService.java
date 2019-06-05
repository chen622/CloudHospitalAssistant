package cn.neuedu.his.service;
import cn.neuedu.his.mapper.RegistrationMapper;
import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface RegistrationService extends Service<Registration> {
    void registerRegistrationInfo(Integer registrarId, Integer patientId, Integer scheduleId, Boolean needBook) throws IllegalArgumentException, IndexOutOfBoundsException;
    void retreatRegistrationInfo(Integer registrationId, Integer registrarId);
    List<Registration> getAllWaitingRegistration(Integer doctorID, Integer state, Date time);
    List<Registration> getHadRegistration(Integer doctorID, Integer state, Date time);
    List<Registration> getFinishRegistration(Integer doctorID, Integer state, Date time);
    List<Registration> getRegistrationByPatientName(String name,Integer doctorID,Integer state);
    ArrayList<Registration> findAllRegistrationWaitingByPatientId(Integer patientId);
    ArrayList<Integer> getAllByDoctor(Integer doctorId, String start, String end, Integer state);
}
