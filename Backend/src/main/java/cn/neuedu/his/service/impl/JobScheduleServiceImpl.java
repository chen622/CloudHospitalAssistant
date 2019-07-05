package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.JobScheduleMapper;
import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.model.ScheduleRule;
import cn.neuedu.his.service.JobScheduleService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.SimpleFormatter;

/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class JobScheduleServiceImpl extends AbstractService<JobSchedule> implements JobScheduleService {

    @Autowired
    private JobScheduleMapper jobScheduleMapper;


    @Override
    public JobSchedule getByDoctorId(Integer doctorId, Date date) {
        return jobScheduleMapper.getByDoctorId(doctorId, date);
    }

    @Override
    public Integer getRegistrationInfo(Date time, Integer doctorId) {
        return jobScheduleMapper.getRegistrationInfo(time, doctorId);
    }

    /**
     * 获取排班信息
     *
     * @return
     */
    @Override
    public ArrayList<JobSchedule> getScheduleToday(Integer departmentId) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Integer hour = Integer.parseInt(formatter.format(date));
        return jobScheduleMapper.getScheduleByPeriod(date, hour, departmentId, null);
    }

    /**
     * 获取两天后排班信息
     *
     * @param departmentId
     * @return
     */
    @Override
    public ArrayList<JobSchedule> getSchedulePre(Integer departmentId) {
        Date date = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Date secondDate = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
        return jobScheduleMapper.getScheduleByPeriod(date, 0, departmentId, secondDate);
    }

    @Override
    public ArrayList<JobSchedule> getScheduleByDoctorIdAndDate(Integer doctorId, Date date) {
        return jobScheduleMapper.getScheduleByDoctorIdAndDate(doctorId, date);
    }

    @Override
    public ArrayList<JobSchedule> getScheduleByMonth(Integer departmentId, Date date) {
        return jobScheduleMapper.getScheduleByMonth(date, departmentId);
    }

    @Override
    public void removeByDate(Integer departmentId, Date date) {
        jobScheduleMapper.removeByDate(departmentId, date);
    }

    @Override
    public JobSchedule generateByRule(ScheduleRule scheduleRule, Date date) {
        JobSchedule jobSchedule = new JobSchedule();
        jobSchedule.setDoctorId(scheduleRule.getDoctorId());
        jobSchedule.setRegistrationTypeId(scheduleRule.getRegistrationTypeId());
        jobSchedule.setIsValid(false);
        jobSchedule.setLimitRegistrationAmount(scheduleRule.getRegistrationQuantity());
        jobSchedule.setPeriod(scheduleRule.getPeriod());
        jobSchedule.setDate(date);
        jobSchedule.setCreateTime(new Date(System.currentTimeMillis()));
        this.save(jobSchedule);
        return jobSchedule;
    }

    @Override
    public ArrayList<JobSchedule> getAfterThreeDays() {
        Date date = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
        return jobScheduleMapper.getByDate(date);
    }

    @Override
    public ArrayList<JobSchedule> findByDate(Date date) {
        return jobScheduleMapper.getByDate(date);
    }
}
