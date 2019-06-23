package cn.neuedu.his.controller;


import cn.neuedu.his.service.JobScheduleService;
import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/wechat")
public class WeChatController {
    @Autowired
    PatientService patientService;
    @Autowired
    JobScheduleService jobScheduleService;

    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject requestJson) {
        try {
            JSONObject returnJson = patientService.parseMiniProgramLoginRequest(requestJson);
            return CommonUtil.successJson(returnJson);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    /**
     * 获取明后两天某医生值班情况
     * @param doctorId
     * @param authentication
     * @return
     */
    @GetMapping("/getSchedule/{doctorId}")
    public JSONObject getSchedule(@PathVariable("doctorId") Integer doctorId, Authentication authentication) {
        JSONObject result = new JSONObject();
        try {
            PermissionCheck.getIdByPatient(authentication);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.add(Calendar.DAY_OF_MONTH,1);
            result.put("1", jobScheduleService.getScheduleByDoctorIdAndDate(doctorId,calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH,1);
            result.put("2", jobScheduleService.getScheduleByDoctorIdAndDate(doctorId,calendar.getTime()));
            return CommonUtil.successJson(result);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

    }
}
