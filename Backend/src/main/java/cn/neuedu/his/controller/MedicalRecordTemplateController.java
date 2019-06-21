package cn.neuedu.his.controller;

import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.MedicalRecordTemplateService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/MRT")
public class MedicalRecordTemplateController {

    @Autowired
    MedicalRecordTemplateService medicalRecordTemplateService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    UserService userService;

    /**
     * 门诊医生查看病例模板
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getMRT")
    public JSONObject getHospitalMR(Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            User user = userService.findById(doctorID);
            if (user == null) {
                return CommonUtil.errorJson(ErrorEnum.E_502);
            }
            return CommonUtil.successJson(medicalRecordTemplateService.getWhichICanUse(doctorID, user.getDepartmentId()));
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }


    @GetMapping("/getMRTByName/{name}")
    public JSONObject getMedicalRecordTemByName(@PathVariable("name") String name, Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return doctorService.getMeicalRecordTemByName(name);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    /**
     * 存为全院病历模板
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveMRT")
    public JSONObject saveMRT(@RequestBody JSONObject object, Authentication authentication) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer doctorID = null;
        Integer level=JSONObject.parseObject(object.get("level").toString(), Integer.class);
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            if(level.equals(Constants.HOSPITALLEVEL)){
                PermissionCheck.isChiefDoctor(doctorService.findById(doctorID).getTitleId());
            }else if(level.equals(Constants.DEPTLEVEL)){
                PermissionCheck.aboveDeputyChiefDoctor(doctorService.findById(doctorID).getTitleId());
            }else if(level.equals(Constants.PERSONALLEVEL)) {
                PermissionCheck.aboveATTENDING_DOCTOR(doctorService.findById(doctorID).getTitleId());
            }else {
                return CommonUtil.errorJson(ErrorEnum.E_502);
            }
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        String name = (String) object.get("name");
        if (name == null || name.equals(""))
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));
        MedicalRecord record = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        return CommonUtil.successJson(doctorService.saveMRTemplate(record, doctorID, name, level));
    }


    @PostMapping("/updateMRT")
    public JSONObject updateMedicalRecordTem(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            PermissionCheck.aboveATTENDING_DOCTOR(doctorService.findById(doctorID).getTitleId());

        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        MedicalRecordTemplate record = JSONObject.parseObject(object.get("medicalRecordTemplate").toString(), MedicalRecordTemplate.class);
        String name = record.getName();
        if (name == null || name.equals(""))
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));

        return CommonUtil.successJson(doctorService.updateMedicalRecordTem(record, doctorID));
    }

    @GetMapping("/deleteMRT/{id}")
    public JSONObject deleteMedicalRecordTemp(@PathVariable("id") Integer id, Authentication authentication) {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            PermissionCheck.aboveATTENDING_DOCTOR(doctorService.findById(doctorID).getTitleId());

        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        return doctorService.deleteMedicalRecordTemp(id, doctorID);
    }

}
