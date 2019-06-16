package cn.neuedu.his.controller;

import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.service.JobScheduleService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/job_schedule")
public class JobScheduleController {

    @Autowired
    JobScheduleService jobScheduleService;

    @GetMapping("/getSchedule/{departmentId}")
    public JSONObject getSchedule(@PathVariable("departmentId") Integer departmentId, Authentication authentication) {
        JSONObject result = new JSONObject();
        //获取挂号收费员id
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        result.put("schedule", jobScheduleService.getScheduleToday(departmentId));

        return CommonUtil.successJson(result);
    }

    @GetMapping("/getScheduleWithMonth/{departmentId}/{date}")
    public JSONObject getScheduleByMonth(@PathVariable("departmentId") Integer departmentId, @PathVariable("date") String date, Authentication authentication) {
        Date day = new Date(Long.parseLong(date));
        try {
            PermissionCheck.isHosptialAdim(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        ArrayList<JobSchedule> jobSchedules = jobScheduleService.getScheduleByMonth(departmentId, day);
        if (jobSchedules == null) {
            jobSchedules = new ArrayList<>();
        }
        return CommonUtil.successJson(jobSchedules);
    }
}
