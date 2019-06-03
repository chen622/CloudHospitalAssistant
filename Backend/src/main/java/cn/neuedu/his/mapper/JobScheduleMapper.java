package cn.neuedu.his.mapper;

import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.util.inter.MyMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component

public interface JobScheduleMapper extends MyMapper<JobSchedule> {
    void updateHaveRegistrationAmountAdd(Integer id);
    void updateHaveRegistrationAmountReduce(Integer id);
    void uodateHaveRegistration(Integer doctorId, Date date);
    JobSchedule getByDoctorId(Integer doctorId, Date date);
    HashMap<String ,Integer> getRegistrationInof(Date time, Integer doctorId);
}