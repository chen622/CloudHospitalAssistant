package cn.neuedu.his.controller;

import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.MedicalRecordTemplateService;
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
@RequestMapping("/medical_record_template")
public class MedicalRecordTemplateController {

    @Autowired
    MedicalRecordTemplateService medicalRecordTemplateService;

    @Autowired
    DoctorService doctorService;

    /**
     * 门诊医生查看病例模板
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getMRTemplate/{level}")
    public JSONObject getHospitalMR(@PathVariable("level") Integer level, Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            if (level.equals(Constants.HOSPITALLEVEL) || level.equals(Constants.PERSONALLEVEL) || level.equals(Constants.DEPTLEVEL))
                return CommonUtil.successJson(doctorService.getHospitalMR(doctorID, level));
            else
                return CommonUtil.errorJson(ErrorEnum.E_709);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }


    @GetMapping("/getMedicalRecordTemByName/{name}")
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
    @PostMapping("/saveHospitalMRTemplate")
    public JSONObject saveHospitalMRTemplate(@RequestBody JSONObject object, Authentication authentication) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            PermissionCheck.isChiefDoctor(doctorService.findById(doctorID).getTitleId());

        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        String name = (String) object.get("name");
        if (name == null || name.equals(""))
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));
        MedicalRecord record = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        return CommonUtil.successJson(doctorService.saveMRTemplate(record, doctorID, name, Constants.HOSPITALLEVEL));
    }

    /**
     * 存为科室病历模板
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveDeptMRTemplate")
    public JSONObject saveDeptMRTemplate(@RequestBody JSONObject object, Authentication authentication) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            PermissionCheck.aboveDeputyChiefDoctor(doctorService.findById(doctorID).getTitleId());

        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        String name = (String) object.get("name");
        if (name == null || name.equals(""))
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));
        MedicalRecord record = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        return CommonUtil.successJson(doctorService.saveMRTemplate(record, doctorID, name, Constants.DEPTLEVEL));
    }

    /**
     * 存为个人病历模板
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/savePersonalMRTemplate")
    public JSONObject savePersonalMRTemplate(@RequestBody JSONObject object, Authentication authentication) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            PermissionCheck.aboveATTENDING_DOCTOR(doctorService.findById(doctorID).getTitleId());
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        String name = (String) object.get("name");
        if (name == null || name.equals(""))
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));
        MedicalRecord record = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        return CommonUtil.successJson(doctorService.saveMRTemplate(record, doctorID, name, Constants.PERSONALLEVEL));
    }

    @PostMapping("/updateMedicalRecordTemp")
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

    @GetMapping("/deleteMedicalRecordTemp/{id}")
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
