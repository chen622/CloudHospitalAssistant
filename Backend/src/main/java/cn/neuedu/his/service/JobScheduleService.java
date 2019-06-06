package cn.neuedu.his.service;
import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.util.inter.Service;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface JobScheduleService extends Service<JobSchedule> {
    void addHaveRegistrationAmount(Integer id);
    void reduceRegistrationAmount(Integer id);
    void uodateHaveRegistration(Integer doctorId, Date date);
    JobSchedule getByDoctorId(Integer doctorId, Date date);
    Integer getRegistrationInof(Date time, Integer doctorId);
}
