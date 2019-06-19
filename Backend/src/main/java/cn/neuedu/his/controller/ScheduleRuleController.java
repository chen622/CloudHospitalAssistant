package cn.neuedu.his.controller;

import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.model.ScheduleRule;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.RegistrationTypeService;
import cn.neuedu.his.service.ScheduleRuleService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/schedule_rule")
public class ScheduleRuleController {

    @Autowired
    ScheduleRuleService scheduleRuleService;
    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    RegistrationTypeService registrationTypeService;

    @PostMapping("/use")
    public JSONObject useRule(@RequestBody JSONObject requestBody, Authentication authentication) {
        try {
            PermissionCheck.isHospitalAdmin(authentication);
            Integer departmentId = requestBody.getInteger("departmentId");
            Date startDate = requestBody.getDate("start");
            Date endDate = requestBody.getDate("end");

            if (departmentId == null || startDate == null || endDate == null) {
                return CommonUtil.errorJson(ErrorEnum.E_501);
            }

            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);

            List<JobSchedule> jobSchedules = new ArrayList<>();
            while (start.compareTo(end) <= 0) {
                jobSchedules.addAll(scheduleRuleService.useRuleToGenerateSchedule(departmentId, start));
                start.add(Calendar.DAY_OF_YEAR, 1);
            }
            return CommonUtil.successJson(jobSchedules);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
    }

    @GetMapping("/getByDepartmentId/{departmentId}")
    public JSONObject getByDepartmentId(@PathVariable("departmentId") Integer departmentId, Authentication authentication) {
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        List<ScheduleRule> scheduleRules = scheduleRuleService.getDoctorScheduleByDepartmentId(departmentId);
        if (scheduleRules == null) {
            scheduleRules = new ArrayList<>();
        }
        return CommonUtil.successJson(scheduleRules);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteScheduleRule(@PathVariable("id") Integer id, Authentication authentication) {

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        scheduleRuleService.deleteById(id);

        return CommonUtil.successJson(id);
    }

    @PostMapping("/insert")
    public JSONObject insertScheduleRule(@RequestBody JSONObject jsonObject, Authentication authentication) {
        //检查权限
        try {
            Integer operatorId = PermissionCheck.isHospitalAdmin(authentication);
            ScheduleRule scheduleRule = JSONObject.toJavaObject(jsonObject, ScheduleRule.class);
            scheduleRule.setOperatorId(operatorId);
            scheduleRuleService.insertScheduleRule(scheduleRule);
            return CommonUtil.successJson();
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        } catch (RuntimeException e) {
            switch (e.getMessage()) {
                case "616":
                    return CommonUtil.errorJson(ErrorEnum.E_616);
                case "617":
                    return CommonUtil.errorJson(ErrorEnum.E_617);
                case "618":
                    return CommonUtil.errorJson(ErrorEnum.E_618);
                case "619":
                    return CommonUtil.errorJson(ErrorEnum.E_619);
            }
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }


    @PostMapping("/modify")
    public JSONObject modifyScheduleRule(@RequestBody JSONObject jsonObject, Authentication authentication) {

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }
        try {
            ScheduleRule scheduleRule = JSONObject.toJavaObject(jsonObject, ScheduleRule.class);
            scheduleRuleService.modifyScheduleRule(scheduleRule);
            return CommonUtil.successJson();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("616"))
                return CommonUtil.errorJson(ErrorEnum.E_616);
            else if (e.getMessage().equals("617"))
                return CommonUtil.errorJson(ErrorEnum.E_617);
            else if (e.getMessage().equals("618"))
                return CommonUtil.errorJson(ErrorEnum.E_618);
            else if (e.getMessage().equals("619"))
                return CommonUtil.errorJson(ErrorEnum.E_619);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }

    }

    @GetMapping("/select/{doctorId}")
    public JSONObject selectScheduleRule(@PathVariable("doctorId") Integer doctorId, Authentication authentication) {

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        List<ScheduleRule> scheduleRules = scheduleRuleService.getDoctorScheduleByDoctorId(doctorId);

        return CommonUtil.successJson(scheduleRules);
    }
}
