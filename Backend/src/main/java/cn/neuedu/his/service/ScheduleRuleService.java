package cn.neuedu.his.service;

import cn.neuedu.his.model.ScheduleRule;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 * Created by ccm on 2019/05/24.
 */
public interface ScheduleRuleService extends Service<ScheduleRule> {
    ScheduleRule getLegalSchedule(Integer doctorId, Integer period);

    List<ScheduleRule> getDoctorScheduleByDoctorId(Integer doctorId);

    List<ScheduleRule> getDoctorScheduleByDepartmentId(Integer departmentId);

    void insertScheduleRule(ScheduleRule scheduleRule);

    void modifyScheduleRule(ScheduleRule scheduleRule);


}
