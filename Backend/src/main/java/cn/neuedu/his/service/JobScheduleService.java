package cn.neuedu.his.service;

import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.model.ScheduleRule;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ccm on 2019/05/24.
 */
public interface JobScheduleService extends Service<JobSchedule> {

    JobSchedule getByDoctorId(Integer doctorId, Date date);

    ArrayList<JobSchedule> getAfterThreeDays();

    Integer getRegistrationInfo(Date time, Integer doctorId);

    ArrayList<JobSchedule> getScheduleToday(Integer departmentId);

    ArrayList<JobSchedule> getScheduleByDoctorIdAndDate(Integer doctorId,Date date);

    ArrayList<JobSchedule> getScheduleByMonth(Integer departmentId, Date date);

    void removeByDate(Date date);

    JobSchedule generateByRule(ScheduleRule scheduleRule,Date date);
}
