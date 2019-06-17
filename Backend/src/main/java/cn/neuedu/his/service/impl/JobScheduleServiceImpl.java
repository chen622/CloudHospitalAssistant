package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.JobScheduleMapper;
import cn.neuedu.his.model.JobSchedule;
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
        return jobScheduleMapper.getScheduleByPeriod(date, hour, departmentId);
    }

    @Override
    public ArrayList<JobSchedule> getScheduleByMonth(Integer departmentId, Date date) {
        return jobScheduleMapper.getScheduleByMonth(date, departmentId);
    }

    @Override
    public ArrayList<JobSchedule> getAfterThreeDays() {
        Date date = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
        return jobScheduleMapper.getByDate(date);
    }
}
