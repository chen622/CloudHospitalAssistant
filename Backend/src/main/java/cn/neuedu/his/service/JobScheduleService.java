package cn.neuedu.his.service;
import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface JobScheduleService extends Service<JobSchedule> {

    void uodateHaveRegistration(Integer doctorId, Date date);
    JobSchedule getByDoctorId(Integer doctorId, Date date);
    ArrayList<JobSchedule> getAfterThreeDays();
    Integer getRegistrationInof(Date time, Integer doctorId);
    ArrayList<JobSchedule> getScheduleToday(Integer departmentId);
}
