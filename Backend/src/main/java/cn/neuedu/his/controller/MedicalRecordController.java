package cn.neuedu.his.controller;

import cn.neuedu.his.model.*;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.MedicalRecordService;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.Constants;
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
@RequestMapping("/medical_record")
public class MedicalRecordController {

    @Autowired
    MedicalRecordService medicalRecordService;
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    RegistrationService registrationService;

    /**
     * 暂存病历
     *
     * @param object
     * @return
     */
    @PostMapping("/saveTemporaryMR")
    public JSONObject saveTemporaryMR(@RequestBody JSONObject object, Authentication authentication) {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        Integer registrationId = object.getInteger("registrationId");
        if (registrationId == null) {
            return CommonUtil.errorJson(ErrorEnum.E_501);
        }
        MedicalRecord medicalRecord = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        medicalRecord.setRegistrationId(registrationId);
        try {
            redisService.setTemporaryMedicalRecord(registrationId, medicalRecord);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_801);
        }
        return CommonUtil.successJson();
    }

    /**
     * 获得暂存病历
     *
     * @param registrationId
     * @return
     */
    @GetMapping("/getTemporaryMR/{registrationId}")
    public JSONObject getTemporaryMR(@PathVariable("registrationId") Integer registrationId) {
        try {
            MedicalRecord record = redisService.getTemporaryMedicalRecord(registrationId);
            if (record==null)
                return CommonUtil.errorJson(ErrorEnum.E_638);
            return CommonUtil.successJson(record);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    /**
     * 删除暂存病历
     *
     * @param registrationId
     * @return
     */
    @GetMapping("/deleteTemporaryMR/{registrationId}")
    public JSONObject deleteTemporaryMR(@PathVariable("registrationId") Integer registrationId) {
        try {
            redisService.deleteTemporaryMR(registrationId);
            return CommonUtil.successJson();
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_803);
        }
    }

    /**
     * 通过患者id获得该患者所有的病历
     *
     * @param patientID
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    @GetMapping("/getAllRecord/{patientID}")
    public JSONObject getAllRecordByPatientId(@PathVariable("patientID") Integer patientID, Authentication authentication) {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        List<MedicalRecord> list = medicalRecordService.getAllByPatientId(patientID);
        if (list == null) {
            list = new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }
    /**
     * 通过患者id获得该患者所有的病历
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    @GetMapping("/getAllRecordWithout")
    public JSONObject getAllRecordWithout( Authentication authentication) {
        try {
            Integer id=PermissionCheck.getIdByPatient(authentication);
            List<MedicalRecord> list = medicalRecordService.getAllByPatientIdTwo(id);
            if (list == null) {
                list = new ArrayList<>();
            }
            return CommonUtil.successJson(list);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
    }




    /**
     * 检查病历是否存在
     *
     * @param registrationId
     * @return
     */
    @GetMapping("/check/{registrationId}")
    public JSONObject check(@PathVariable("registrationId") Integer registrationId) {
        MedicalRecord medicalRecord = medicalRecordService.getByRegistrationId(registrationId);
        if (medicalRecord == null) {
            return CommonUtil.errorJson(ErrorEnum.E_710);
        } else {
            return CommonUtil.successJson(medicalRecord);
        }
    }

    /**
     * 修改挂号状态
     *
     * @param registrationId
     * @return
     */
    @PostMapping("/comein/{registrationId}")
    public JSONObject comein(@PathVariable("registrationId") Integer registrationId) {
        Registration registration = registrationService.findById(registrationId);
        if (registration == null || !registration.getState().equals(Constants.WAITING_FOR_TREATMENT)) {
            return CommonUtil.errorJson(ErrorEnum.E_710);
        } else {
            registration.setState(Constants.INSIDE_DOCTOR);
            registrationService.update(registration);
            return CommonUtil.successJson();
        }
    }

    /**
     * 医生初诊提交，更新该挂号状态
     * update the registration state as first diagnose which is 803
     * +
     *
     * @param object
     * @return
     */
    @PostMapping("/firstDiagnose")
    public JSONObject setFirstDiagnose(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorID;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        Integer registrationID = null;
        registrationID = object.getInteger("registrationId");
        if (registrationID == null)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));

        //删除暂存病历
        try {
            redisService.deleteTemporaryMR(registrationID);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_803);
        }

        MedicalRecord medicalRecord = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        medicalRecord.setRegistrationId(registrationID);
        ArrayList<DiseaseSecond> diagnoses = (ArrayList<DiseaseSecond>) object.getJSONArray("diagnoses").toJavaList(DiseaseSecond.class);
        ArrayList<Integer> diagnosesIds = new ArrayList<>();
        diagnoses.forEach(diseaseSecond -> diagnosesIds.add(diseaseSecond.getId()));
        if (diagnoses.size() == 0)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("diagnoses"));
        JSONObject object1;
        try {
            object1 = doctorService.setFirstDiagnose(registrationID, medicalRecord, diagnosesIds, doctorID);
        } catch (RuntimeException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("medicalRecord"));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
        return object1;
    }


    /**
     * 保存确诊信息
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/finalDiagnose")
    public JSONObject saveFinalDiagnose(@RequestBody JSONObject object, Authentication authentication) {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        Integer registrationID = null;
        try {
            registrationID = Integer.parseInt(object.get("registrationId").toString());
            MedicalRecord medicalRecord = medicalRecordService.getByRegistrationId(registrationID);
            if (medicalRecord == null)
                throw new NumberFormatException();
            ArrayList<DiseaseSecond> diagnoses = (ArrayList<DiseaseSecond>) object.getJSONArray("diagnoses").toJavaList(DiseaseSecond.class);
            ArrayList<Integer> diagnosesIds = new ArrayList<>();
            diagnoses.forEach(diseaseSecond -> diagnosesIds.add(diseaseSecond.getId()));
            if (diagnoses == null || diagnoses.size() == 0)
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("diagnoses"));
            try {
                return doctorService.saveFinalDiagnose(registrationID, medicalRecord, diagnosesIds);
            } catch (Exception e) {
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("medicalRecord"));
            }
        } catch (NumberFormatException n) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        }
    }

    /**
     * 更新病历
     * 传过来的medicalRecord里面的挂号id一定要填
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/updateMR")
    public JSONObject updateMR(@RequestBody JSONObject object, Authentication authentication) {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        Integer registrationID = null;
        try {
            registrationID = Integer.parseInt(object.get("registrationId").toString());
        } catch (NumberFormatException n) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        }
        MedicalRecord record = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        ArrayList<DiseaseSecond> diagnoses = (ArrayList<DiseaseSecond>) object.getJSONArray("diagnoses").toJavaList(DiseaseSecond.class);
        ArrayList<Integer> diagnosesIds = new ArrayList<>();
        diagnoses.forEach(diseaseSecond -> diagnosesIds.add(diseaseSecond.getId()));
        return doctorService.updateMR(registrationID, record, diagnosesIds);
    }

    @GetMapping("/getDrugNonDrugAndResult/{id}")
    public JSONObject getDrugNonDrugAndResultByMedicalId(@PathVariable("id") Integer id, Authentication authentication){
        /*
        try{
            PermissionCheck.getIdByPatient(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }*/

        MedicalRecord medicalRecord = medicalRecordService.getDrugNonDrugAndResultByMedicalId(id);
        return CommonUtil.successJson(medicalRecord);
    }
}
