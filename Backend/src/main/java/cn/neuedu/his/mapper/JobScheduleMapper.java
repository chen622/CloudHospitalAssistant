package cn.neuedu.his.mapper;

import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.util.inter.MyMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Component

public interface JobScheduleMapper extends MyMapper<JobSchedule> {
    void uodateHaveRegistration(Integer doctorId, Date date);
    JobSchedule getByDoctorId(Integer doctorId, Date date);
    ArrayList<JobSchedule> getByDate(Date date);
    Integer getRegistrationInof(Date time, Integer doctorId);
    ArrayList<JobSchedule> getScheduleByPeriod(@Param("date") Date date, @Param("hour") Integer hour, @Param("departmentId") Integer departmentId);
}