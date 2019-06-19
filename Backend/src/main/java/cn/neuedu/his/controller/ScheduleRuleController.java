package cn.neuedu.his.controller;

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
            PermissionCheck.isHospitalAdmin(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try {
            ScheduleRule scheduleRule = JSONObject.toJavaObject(jsonObject, ScheduleRule.class);
            scheduleRuleService.insertScheduleRule(scheduleRule);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("616"))
                return CommonUtil.errorJson(ErrorEnum.E_616);
            else if (e.getMessage().equals("617"))
                return CommonUtil.errorJson(ErrorEnum.E_617);
            else if (e.getMessage().equals("618"))
                return CommonUtil.errorJson(ErrorEnum.E_618);
            else if (e.getMessage().equals("619"))
                return CommonUtil.errorJson(ErrorEnum.E_619);
        }
        return CommonUtil.successJson();
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
