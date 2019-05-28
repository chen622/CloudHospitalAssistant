package cn.neuedu.his.controller;

import cn.neuedu.his.mapper.MedicalRecordMapper;
import cn.neuedu.his.model.InspectionTemplate;
import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.MedicalRecordService;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    MedicalRecordService medicalRecordService;

    /**
     *通过患者id获得该患者所有的病历
     * @param patientID
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    @GetMapping("/getAllRecord/{patientID}")
    public JSONObject getAllRecordByPatientId(@PathVariable("patientID") Integer patientID, Authentication authentication)
    throws AuthenticationServiceException {
        Integer doctorID;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<MedicalRecord> list=medicalRecordService.getAllByPatientId(patientID);
        if (list==null){
            list=new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    /**
     * 通过患者名字找到所有挂在该医生的待诊挂号
     * use patient name to registration
     * @param name
     * @param authentication
     * @return
     */
    @GetMapping("/getByName/{name}")
    public JSONObject getRegistrationByPatientName(@PathVariable("name") String name, Authentication authentication ) {
        Integer doctorID;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<Registration> list=registrationService.getRegistrationByPatientName(name,doctorID,Constants.WAITING_FOR_TREATMENT);
        if (list==null){
            list=new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    /**
     * 找到该医生对应的所有待诊挂号
     * @param authentication
     * @return
     */
    @GetMapping("/getAllWait")
    public JSONObject getAllWaitingRegistration(Authentication authentication){
        Integer doctorID;
        try {
            doctorID=PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<Registration> list=registrationService.getAllWaitingRegistration(doctorID,Constants.WAITING_FOR_TREATMENT);
        if (list==null){
            list=new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    /**
     * 医生初诊提交，更新该挂号状态
     * update the registration state as first diagnose which is 803
     * @param object
     * @return
     */
    @PostMapping("/firstDiagnose")
    public JSONObject setFirstDiagnose(@RequestBody JSONObject object, Authentication authentication) {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        //更新该挂号状态为初诊
        Integer registrationID=Integer.parseInt(object.get("registrationID").toString());
        MedicalRecord medicalRecord = JSONObject.parseObject(object.get("medicalRecord").toString(), MedicalRecord.class);
        medicalRecord.setRegistrationId(registrationID);
        JSONObject object1;
        try {
            object1=doctorService.setFirstDiagnose(registrationID, medicalRecord);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("medicalRecord"));
        }
        return object1;
    }


    /**
     * 门诊医生查看全院模板
     * @param authentication
     * @return
     */
    @GetMapping("/getHospitalCheckTemps")
    public JSONObject getHospitalCheckTemps(Authentication authentication){
        try {
           Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  doctorService.getHospitalCheckTemps(doctorID,Constants.HOSPITALLEVEL);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 门诊医生查看所在科室模板
     * @param authentication
     * @return
     */
    @GetMapping("/getDeptCheckTemps")
    public JSONObject getDeptCheckTemps(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  doctorService.getDeptCheckTemps(doctorID,Constants.DEPTLEVEL);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 门诊医生查看个人模板
     * @param authentication
     * @return
     */
    @GetMapping("/getPersonalCheckTemps")
    public JSONObject getPersonalCheckTemps(Authentication authentication){
        try {
            Integer doctorID=PermissionCheck.isOutpatientDoctor(authentication);
            return  doctorService.getPersonalCheckTemps(doctorID,Constants.PERSONALLEVEL);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * update the registration state as suspect diagnose which is 804
     * 当医生申请检查，应该更新该挂号状态为疑诊
     * @param id
     * @return
     */
    @Transactional
    public JSONObject updateStateToSuspectDiagnose(@RequestBody Integer id, Authentication authentication) {
             try {
                 PermissionCheck.isOutpatientDoctor(authentication);
             }catch (AuthenticationServiceException a){
                 return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
             }
        Registration registration = registrationService.findById(id);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        } else{
            registration.setState(Constants.SUSPECT);
            registrationService.update(registration);
            return CommonUtil.successJson();
        }
    }

    /**
     * update the registration state as suspect diagnose which is 804
     * 当医生点击了确诊的时候，应该更新该挂号状态为确诊
     * @param id
     * @return
     */
    @Transactional
    public JSONObject updateStateToFinalDiagnose(@RequestBody Integer id, Authentication authentication)  {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        Registration registration = registrationService.findById(id);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        } else{
            registration.setState(Constants.FINAL_DIAGNOSIS);
            registrationService.update(registration);
            return CommonUtil.successJson();
        }
    }

    /**
     * update the registration state as suspect diagnose which is 804
     * 当医生提交处方，应该更新该挂号状态为诊毕
     * @param id
     * @return
     */
    @Transactional
    public JSONObject updateStateToFinishDiagnose(@RequestBody Integer id, Authentication authentication){
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        Registration registration = registrationService.findById(id);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        } else{
            registration.setState(Constants.FINISH_DIAGNOSIS);
            registrationService.update(registration);
            return CommonUtil.successJson();
        }
    }





}
