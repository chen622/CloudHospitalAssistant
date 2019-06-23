package cn.neuedu.his.mapper;

import cn.neuedu.his.model.ScheduleRule;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component


public interface ScheduleRuleMapper extends MyMapper<ScheduleRule> {
    ScheduleRule getLegalSchedule(String identifyId, Integer period, Integer day);

    List<ScheduleRule> getDoctorSchedule(Integer doctorId);

    List<ScheduleRule> getFullByDepartmentId(Integer departmentId);

    List<ScheduleRule> getFullByDepartmentIdAndDay(Integer departmentId, Integer day);
}