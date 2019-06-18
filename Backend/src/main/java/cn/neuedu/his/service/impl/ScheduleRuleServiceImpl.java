package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.ScheduleRuleMapper;
import cn.neuedu.his.model.ScheduleRule;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.RegistrationTypeService;
import cn.neuedu.his.service.ScheduleRuleService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class ScheduleRuleServiceImpl extends AbstractService<ScheduleRule> implements ScheduleRuleService {

    @Autowired
    private ScheduleRuleMapper scheduleRuleMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private RegistrationTypeService registrationTypeService;


    @Override
    public List<ScheduleRule> getDoctorScheduleByDoctorId(Integer doctorId) {
        return scheduleRuleMapper.getDoctorSchedule(doctorId);
    }

    @Override
    public List<ScheduleRule> getDoctorScheduleByDepartmentId(Integer departmentId) {
        return scheduleRuleMapper.getFullByDepartmentId(departmentId);
    }

    @Override
    public void insertScheduleRule(ScheduleRule scheduleRule) {

        try {
            check(scheduleRule);
        } catch (RuntimeException e) {
            throw e;
        }

        this.save(scheduleRule);
    }

    @Override
    public void modifyScheduleRule(ScheduleRule scheduleRule) {
        try {
            check(scheduleRule);
        } catch (RuntimeException e) {
            throw e;
        }

        this.update(scheduleRule);
    }

    public void check(ScheduleRule scheduleRule) {
        if (userService.findById(scheduleRule.getOperatorId()) == null)
            throw new RuntimeException("616");
        //return CommonUtil.errorJson(ErrorEnum.E_616);

        //检测医生是否存在
        if (doctorService.findById(scheduleRule.getDoctorId()) == null)
            throw new RuntimeException("617");
        //return CommonUtil.errorJson(ErrorEnum.E_617);

        //检测挂号类型是否存在
        if (registrationTypeService.findById(scheduleRule.getRegistrationTypeId()) == null)
            throw new RuntimeException("618");
        //return CommonUtil.errorJson(ErrorEnum.E_618);

        //判断时间是否冲突
        ScheduleRule scheduleRule1 = scheduleRuleMapper.getLegalSchedule(userService.findById(scheduleRule.getDoctorId()).getIdentifyId()
                , scheduleRule.getPeriod(),scheduleRule.getDay());
        if (scheduleRule1 != null &&scheduleRule.getId()!=scheduleRule1.getId())
            throw new RuntimeException("619");
        //return CommonUtil.errorJson(ErrorEnum.E_619);
    }
}
