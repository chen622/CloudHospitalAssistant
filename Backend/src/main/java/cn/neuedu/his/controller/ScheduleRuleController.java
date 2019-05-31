package cn.neuedu.his.controller;

import cn.neuedu.his.model.RegistrationType;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
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

    @PostMapping("/delete/{id}")
    public JSONObject deleteScheduleRule(@PathVariable Integer id, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        scheduleRuleService.deleteById(id);

        return CommonUtil.successJson(id);
    }

    @PostMapping("/insert")
    public JSONObject insertScheduleRule(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        ScheduleRule scheduleRule = jsonObject.toJavaObject(jsonObject,ScheduleRule.class);

        //检测操作员id是否存在
        if (userService.findById(scheduleRule.getOperatorId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_616);

        //检测医生是否存在
        if (doctorService.findById(scheduleRule.getDoctorId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_617);

        //检测挂号类型是否存在
        if (registrationTypeService.findById(scheduleRule.getRegistrationTypeId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_618);

        //判断时间是否冲突
        if (scheduleRuleService.getLegalSchedule(scheduleRule.getDoctorId(),scheduleRule.getPeriod()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_619);

        scheduleRuleService.save(scheduleRule);
        return CommonUtil.successJson(scheduleRule);
    }


    @PostMapping("/modify")
    public JSONObject modifyScheduleRule(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        ScheduleRule scheduleRule = jsonObject.toJavaObject(jsonObject,ScheduleRule.class);

        //检测操作员id是否存在
        if (userService.findById(scheduleRule.getOperatorId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_616);

        //检测医生是否存在
        if (doctorService.findById(scheduleRule.getDoctorId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_617);

        //检测挂号类型是否存在
        if (registrationTypeService.findById(scheduleRule.getRegistrationTypeId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_618);

        //判断时间是否冲突
        if (scheduleRuleService.getLegalSchedule(scheduleRule.getDoctorId(),scheduleRule.getPeriod()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_619);

        scheduleRuleService.update(scheduleRule);
        return CommonUtil.successJson(scheduleRule);
    }

    @GetMapping("/select/{doctorId}")
    public JSONObject selectScheduleRule(@PathVariable("doctorId") Integer doctorId, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        List<ScheduleRule> scheduleRules = scheduleRuleService.getDoctorSchedule(doctorId);

        return CommonUtil.successJson(scheduleRules);
    }
}
