package cn.neuedu.his.controller;

import cn.neuedu.his.service.JobScheduleService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
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
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        result.put("schedule", jobScheduleService.getScheduleToday(departmentId));

        return CommonUtil.successJson(result);
    }

}
