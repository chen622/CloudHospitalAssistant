package cn.neuedu.his.controller;


import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.model.Patient;
import cn.neuedu.his.model.Patient;
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

import java.util.ArrayList;
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

    @PostMapping("/updatePatient")
    public JSONObject updatePatient(@RequestBody JSONObject requestJson, Authentication authentication) {
        try {
            Integer patientId = PermissionCheck.getIdByPatient(authentication);
            Patient patient = JSONObject.toJavaObject(requestJson, Patient.class);
            patient.setId(patientId);
            if (patient.getIdentityId() == null || patient.getSex() == null || patient.getRealName() == null || patient.getPhoneNumber() == null) {
                return CommonUtil.errorJson(ErrorEnum.E_501);
            }
            patient.setConfirm(true);
            patientService.update(patient);
            return CommonUtil.successJson(patient);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
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
            ArrayList<JobSchedule> list=jobScheduleService.getScheduleByDoctorIdAndDate(doctorId,calendar.getTime());
            if (list==null)
                list=new ArrayList<>();
            calendar.add(Calendar.DAY_OF_MONTH,1);
            list.addAll(jobScheduleService.getScheduleByDoctorIdAndDate(doctorId,calendar.getTime()));
            return CommonUtil.successJson(list);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

    }

    @GetMapping("/getPatient")
    public JSONObject  getPatient(Authentication authentication){
        try{
            Integer id=PermissionCheck.getIdByPatient(authentication);
            Patient patient = patientService.findById(id);
            return CommonUtil.successJson(patient);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
    }
}
