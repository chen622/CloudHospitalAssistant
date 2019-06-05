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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    @Autowired
    RedisServiceImpl redisService;

    /**
     * 暂存病历
     * @param object
     * @return
     */
    @PostMapping("/saveTemporaryMR")
    public JSONObject saveTemporaryMR(@RequestBody  JSONObject object){
        Integer id=Integer.parseInt(object.get("registrationId").toString());
        MedicalRecord record=JSONObject.parseObject(object.get("record").toString(), MedicalRecord.class);
        try {
            redisService.setTemporaryMedicalRecord(id,record);
        } catch (Exception e) {
           return CommonUtil.errorJson(ErrorEnum.E_801);
        }
        return CommonUtil.successJson();
    }

    /**
     * 获得暂存病历
     * @param registrationId
     * @return
     */
    @GetMapping("/getTemporaryMR/{registrationId}")
    public JSONObject getTemporaryMR(@PathVariable("registrationId") Integer registrationId){
        try {
            MedicalRecord record= redisService.getTemporaryMedicalRecord(registrationId);
            if (record==null)
                record=new MedicalRecord();
            return  CommonUtil.successJson(record);
        } catch (Exception e) {
           return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    /**
     * 获得暂存病历
     * @param registrationId
     * @return
     */
    @GetMapping("/deleteTemporaryMR/{registrationId}")
    public JSONObject deleteTemporaryMR(@PathVariable("registrationId") Integer registrationId){
        try {
            redisService.deleteTemporaryMR(registrationId);
            return  CommonUtil.successJson();
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_803);
        }
    }

    /**
     * 暂存检查/处置
     * @param object
     * @return
     */
    @PostMapping("/saveTemporaryInspection")
    public JSONObject saveTemporaryInspection(@RequestBody JSONObject object){
        Integer id=Integer.parseInt(object.get("registrationId").toString());
        List<InspectionApplication> record=JSONObject.parseArray(object.get("inspections").toString(), InspectionApplication.class);
        List<Prescription> prescriptions=JSONObject.parseArray(object.get("prescriptions").toString(), Prescription.class);
        try {
            redisService.setTemporaryInspection(id,record,prescriptions);
        } catch (Exception e) {
           return CommonUtil.errorJson(ErrorEnum.E_801);
        }
        return CommonUtil.successJson();
    }


    /**
     * 获得暂存检查/处置
     * @param registrationId
     * @return
     */
    @GetMapping("/getTemporaryInspection/{registrationId}")
    public JSONObject getTemporaryInspection(@PathVariable("registrationId") Integer registrationId){
        JSONObject object=new JSONObject();
        try {
            List<Prescription> prescriptions=redisService.getTemporaryPrescription(registrationId);
            if(prescriptions==null)
                prescriptions=new ArrayList<>();
            object.put("prescriptions", prescriptions);
            List<InspectionApplication> applications=redisService.getTemporaryApplications(registrationId);
            if(applications==null)
                applications=new ArrayList<>();
            object.put("applications", applications);
            return CommonUtil.successJson(object);
        } catch (Exception e) {
            e.printStackTrace();
           return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    /**
     * 获得暂存病历
     * @param registrationId
     * @return
     */
    @GetMapping("/deleteTemporaryInspection/{registrationId}")
    public JSONObject deleteTemporaryInspection(@PathVariable("registrationId") Integer registrationId){
        try {
            redisService.deleteTemporaryInspection(registrationId);
            return  CommonUtil.successJson();
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_803);
        }
    }











    //PARTONE-门诊病例首页

    /**
     * 通过患者id获得该患者所有的病历
     *
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
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<MedicalRecord> list = medicalRecordService.getAllByPatientId(patientID);
        if (list == null) {
            list = new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    /**
     * 通过患者名字找到所有挂在该医生的待诊挂号
     * use patient name to registration
     *
     * @param name
     * @param authentication
     * @return
     */
    @GetMapping("/getByName/{name}")
    public JSONObject getRegistrationByPatientName(@PathVariable("name") String name, Authentication authentication) {
        Integer doctorID;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<Registration> list = registrationService.getRegistrationByPatientName(name, doctorID, Constants.WAITING_FOR_TREATMENT);
        if (list == null) {
            list = new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    @GetMapping("/getRegistrationInof/{time}")
    public  JSONObject getRegistrationInof(@PathVariable("time") Date time,Authentication authentication){
        Integer doctorID;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        return doctorService.getRegistrationInof(time, doctorID);
    }

    /**
     * 找到该医生当天对应的所有待诊挂号
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getAllRegistration/{time}")
    public JSONObject getAllRegistration(@PathVariable("time") Date time, Authentication authentication) {
        Integer doctorID;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        List<Registration> list = registrationService.getAllWaitingRegistration(doctorID, Constants.WAITING_FOR_TREATMENT, time);
        if (list == null) {
            list = new ArrayList<>();
        }

        List<Registration> list2=registrationService.getAllWaitingRegistration(doctorID, Constants.FIRST_DIAGNOSIS, time);
        if(list2==null)
            list2=new ArrayList<>();
        List<Registration> te=registrationService.getAllWaitingRegistration(doctorID, Constants.SUSPECT, time);
        if(te!=null)
            list2.addAll(te);
        te=registrationService.getAllWaitingRegistration(doctorID, Constants.FINAL_DIAGNOSIS, time);
        if (te!=null)
            list2.addAll(te);

        List<Registration> finish=registrationService.getAllWaitingRegistration(doctorID, Constants.FINISH_DIAGNOSIS, time);
       if(finish==null)
           finish=new ArrayList<>();
        HashMap<Integer ,List<Registration>> map=new HashMap<>();
        map.put(Constants.WAITING_FOR_TREATMENT,list);
        map.put(Constants.FIRST_DIAGNOSIS,list2);
        map.put(Constants.FINISH_DIAGNOSIS, finish);
        return CommonUtil.successJson(map);
    }


    /**
     * 门诊医生查看病例模板
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getMRTemplate/{level}")
    public JSONObject getHospitalMR(@PathVariable("level")Integer level, Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            if(level.equals(Constants.HOSPITALLEVEL)|| level.equals(Constants.PERSONALLEVEL)|| level.equals(Constants.DEPTLEVEL))
                return CommonUtil.successJson(doctorService.getHospitalMR(doctorID, level));
            else
                return CommonUtil.errorJson(ErrorEnum.E_709);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

//    /**
//     * 门诊医生查看所在科室病例模板
//     *
//     * @param authentication
//     * @return
//     */
//    @GetMapping("/getDeptMR")
//    public JSONObject getDeptMR(Authentication authentication) {
//        try {
//            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
//            return CommonUtil.successJson(doctorService.getDeptMR(doctorID, Constants.DEPTLEVEL));
//        } catch (AuthenticationServiceException a) {
//            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
//        }
//    }
//
//    /**
//     * 门诊医生查看个人病例模板
//     *
//     * @param authentication
//     * @return
//     */
//    @GetMapping("/getPersonalMR")
//    public JSONObject getPersonalMR(Authentication authentication) {
//        try {
//            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
//            return CommonUtil.successJson(doctorService.getPersonalMR(doctorID, Constants.PERSONALLEVEL));
//        } catch (AuthenticationServiceException a) {
//            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
//        }
//    }

    @GetMapping("/getMedicalRecordTemByName/{name}")
    public JSONObject getMedicalRecordTemByName(@PathVariable("name")String name,Authentication authentication){
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return doctorService.getMeicalRecordTemByName(name);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 通过部分连续的字段获得所有疾病
     *
     * @param name
     * @param authentication
     */
    @GetMapping("/findDisease/{name}")
    public JSONObject findDiseaseByName(@PathVariable("name") String name, Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return CommonUtil.successJson(doctorService.findDiseaseByName(name));
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    @GetMapping("/getAllDiseases")
    public JSONObject getAllDiseases(Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return CommonUtil.successJson(doctorService.getAllDiease());
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 通过部分连续的字段获得所有非药
     *
     * @param name
     * @param authentication
     */
    @GetMapping("/findNonDrug/{name}")
    public JSONObject findNonDrugByName(@PathVariable("name") String name, Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            List<NonDrug> nonDrugs = doctorService.findNonDrugByName(name);
            return CommonUtil.successJson(nonDrugs);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 获得所有非药物品
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getAllNonDrug")
    public JSONObject getAllNonDrug(Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return CommonUtil.successJson(doctorService.getAllNonDrug());
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }


    /**
     * 医生初诊提交，更新该挂号状态
     * update the registration state as first diagnose which is 803
     *
     * @param object
     * @return
     */
    @PostMapping("/firstDiagnose")
    public JSONObject setFirstDiagnose(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        Integer registrationID = null;
        try {
            registrationID = Integer.parseInt(object.get("registrationId").toString());
            if (registrationID == null)
                throw new NumberFormatException();
        } catch (NumberFormatException n) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        }
        MedicalRecord medicalRecord = JSONObject.parseObject(object.get("medicalRecordId").toString(), MedicalRecord.class);
        medicalRecord.setRegistrationId(registrationID);
        ArrayList<Integer> diagnoses = (ArrayList<Integer>) object.getJSONArray("diagnoses").toJavaList(Integer.class);
        if (diagnoses == null || diagnoses.size() == 0)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("diagnoses"));
        JSONObject object1;
        try {
            object1 = doctorService.setFirstDiagnose(registrationID, medicalRecord, diagnoses, doctorId);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("medicalRecord"));
        }
        return object1;
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
            isChiefDoctor(doctorID);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
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
            aboveDeputyChiefDoctor(doctorID);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
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
            aboveATTENDING_DOCTOR(doctorID);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
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
            aboveATTENDING_DOCTOR(doctorID);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }

        MedicalRecordTemplate record = JSONObject.parseObject(object.get("medicalRecordTemplate").toString(), MedicalRecordTemplate.class);
        String name =record.getName();
        if (name == null || name.equals(""))
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("name"));

        JSONObject k = checkTemplate("medicalRecord", doctorID, record.getName(), record.getLevelId());
        if (k != null)
            return k;
        return CommonUtil.successJson(doctorService.updateMedicalRecordTem(record, doctorID));
    }

    @GetMapping("/deleteMedicalRecordTemp/{id}")
    public JSONObject deleteMedicalRecordTemp(@PathVariable("id") Integer id, Authentication authentication) {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            aboveATTENDING_DOCTOR(doctorID);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
        return doctorService.deleteMedicalRecordTemp(id, doctorID);
    }

    /**
     * 主任医师权限检验
     *
     * @param id 医生
     * @return
     * @throws AuthenticationServiceException
     */
    @Transactional
    public boolean isChiefDoctor(Integer id) throws AuthenticationServiceException {
        Doctor doctor = doctorService.findById(id);
        if (doctor.getTitleId().equals(Constants.CHIEF_DOCTOR)) {
            return true;
        } else {
            throw new AuthenticationServiceException("ChiefDoctor");
        }
    }


    /**
     * 副主任医师以及以上权限检验
     *
     * @param id 医生
     * @return
     * @throws AuthenticationServiceException
     */
    @Transactional
    public boolean aboveDeputyChiefDoctor(Integer id) throws AuthenticationServiceException {
        Doctor doctor = doctorService.findById(id);
        if (doctor.getTitleId().equals(Constants.DEPUTY_CHIEF_DOCTOR) || doctor.getTitleId().equals(Constants.CHIEF_DOCTOR)) {
            return true;
        } else {
            throw new AuthenticationServiceException("DeputyChiefDocto");
        }
    }


    /**
     * 主治医师以及以上权限检验
     *
     * @param id 医生
     * @return
     * @throws AuthenticationServiceException
     */
    @Transactional
    public boolean aboveATTENDING_DOCTOR(Integer id) throws AuthenticationServiceException {
        Doctor doctor = doctorService.findById(id);
        if (doctor.getTitleId().equals(Constants.DEPUTY_CHIEF_DOCTOR) || doctor.getTitleId().equals(Constants.CHIEF_DOCTOR) || doctor.getTitleId().equals(Constants.ATTENDING_DOCTOR)) {
            return true;
        } else {
            throw new AuthenticationServiceException("ATTENDING_DOCTOR");
        }
    }


    //PARTTWO-检查/检验/申请
    @GetMapping("/openInspection")
    public JSONObject openInspection(JSONObject object, Authentication authentication) {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
        try {
            Integer registrationId = Integer.parseInt(object.get("registrationId").toString());
            return doctorService.openInspection(registrationId);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        }
    }


    /**
     * 门诊医生查看全院检查模板
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getHospitalCheckTemps")
    public JSONObject getHospitalCheckTemps(Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return doctorService.getHospitalCheckTemps(doctorID, Constants.HOSPITALLEVEL);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 门诊医生查看所在科室检查模板
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getDeptCheckTemps")
    public JSONObject getDeptCheckTemps(Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return (doctorService.getDeptCheckTemps(doctorID, Constants.DEPTLEVEL));
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 门诊医生查看个人检查模板
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getPersonalCheckTemps")
    public JSONObject getPersonalInspectionTemps(Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            JSONObject object1 = (doctorService.getPersonalCheckTemps(doctorID, Constants.PERSONALLEVEL));
            return object1;
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    /**
     * 通过名字查找检查模板
     * @param name
     * @param authentication
     * @return
     */
    @GetMapping("/getInspectionTemByName/{name}")
    public JSONObject getInspectionTemByName(@PathVariable("name")String name,Authentication authentication){
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return doctorService.getInspectionTemByName(name);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }


    /**
     * 保存医生申请的非药项目
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveInspection")
    public JSONObject saveInspection(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
        try {
            //是否为确诊的
            Boolean isDisposal = (Boolean) object.get("isDisposal");
            return doctorService.saveInspection(object, isDisposal, doctorId);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }
    }


    /**
     * 保存检查模板
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveInspectionTem")
    public JSONObject saveInspectionTem(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        InspectionTemplate template = JSONObject.parseObject(object.get("template").toString(), InspectionTemplate.class);
        JSONObject k = checkTemplate("inspection", doctorId, template.getName(), template.getLevel());
        if (k != null)
            return k;
        try {
            return doctorService.saveInspectionAsTemplate(template, doctorId);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }
    }

    /**
     * 更新检查模板
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/updateInspectionTem")
    public JSONObject updateInspectionTem(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        InspectionTemplate template = JSONObject.parseObject(object.get("template").toString(), InspectionTemplate.class);
        Integer crearedById = template.getCreatedById();
        if (!crearedById.equals(doctorId))
            return CommonUtil.errorJson(ErrorEnum.E_706.addErrorParamName("InspectionTemplate"));

        JSONObject k = checkTemplate("inspection", doctorId, template.getName(), template.getLevel());
        if (k != null)
            return k;
        try {
            return doctorService.updateInspectionTem(template, doctorId);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }
    }

    @GetMapping("/deleteInspectionTemp/{id}")
    public JSONObject deleteInspectionTemp(@PathVariable("id") Integer id, Authentication authentication) {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            aboveATTENDING_DOCTOR(doctorID);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        }
        return doctorService.deleteInspectionTemp(id, doctorID);
    }


    /**
     * 获得检查结果
     *
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping("/getResult/{id}")
    public JSONObject getInspectionResult(@PathVariable("id") Integer id, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        return doctorService.getInspectionResult(id);
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
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        Integer registrationID = null, medicalRecordId;
        try {
            registrationID = Integer.parseInt(object.get("registrationId").toString());
            if (registrationID == null)
                throw new NumberFormatException();
        } catch (NumberFormatException n) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        }
        try {
            medicalRecordId = Integer.parseInt(object.get("medicalRecordId").toString());
            if (registrationID == null)
                throw new NumberFormatException();
        } catch (NumberFormatException n) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("medicalRecordId"));
        }

        ArrayList<Integer> diagnoses = (ArrayList<Integer>) object.getJSONArray("diagnoses").toJavaList(Integer.class);
        if (diagnoses == null || diagnoses.size() == 0)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("diagnoses"));
        try {
            return doctorService.saveFinalDiagnose(registrationID, medicalRecordId, diagnoses);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("medicalRecord"));
        }
    }


    //part three

    /**
     * 保存医生开的药方
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/savePrescriptions")
    public JSONObject savePrescriptions(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            try{
                doctorId=PermissionCheck.isTechnicalDoctor(authentication);
            }catch (AuthenticationServiceException aa){
                return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
            }
        }
        Integer medicalId = Integer.parseInt(object.get("medicalRecordId").toString());
        Integer registationId = Integer.parseInt(object.get("medicalRecordId").toString());

        List<Prescription> prescriptions = JSONObject.parseArray(object.get("prescriptions").toString(), Prescription.class);
        if (prescriptions != null && !prescriptions.isEmpty()) {
            try {
                return doctorService.savePrescriptions(prescriptions, medicalId, registationId,doctorId);
            } catch (Exception e) {
                return CommonUtil.errorJson(ErrorEnum.E_500.addErrorParamName(e.getMessage()));
            }
        } else {
            return CommonUtil.errorJson(ErrorEnum.E_704);
        }
    }


    /**
     * 保存药房模板
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/savePrescriptionTemp")
    public JSONObject savePrescriptionsTemp(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }

        Integer medicalId = Integer.parseInt(object.get("medicalRecordId").toString());
//        Integer registationId=Integer.parseInt(object.get("registrationId").toString());

        DrugTemplate template = JSONObject.parseObject(object.get("template").toString(), DrugTemplate.class);
        JSONObject k = checkTemplate("drugTemplate", doctorId, template.getName(), template.getLevel());
        if (k != null)
            return k;

        if (template != null && !template.getPrescriptions().isEmpty()) {
            try {
                return doctorService.savePrescriptionsTemp(template, medicalId, doctorId);
            } catch (Exception e) {
                return CommonUtil.errorJson(ErrorEnum.E_500.addErrorParamName(e.getMessage()));
            }
        } else {
            return CommonUtil.errorJson(ErrorEnum.E_704);
        }
    }

    @GetMapping("/getPrescriptionsTemByName/{name}")
    public JSONObject getPrescriptionsTemByName(@PathVariable("name")String name,Authentication authentication){
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return doctorService.getPrescriptionsTemByName(name);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
    }

    @GetMapping("/finishDiagnose/{registrationId}")
    public JSONObject finishDiagnose(@PathVariable("registrationId") Integer registrationId, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        return doctorService.finishDiagnose(registrationId);
    }

    @GetMapping("/paymentDetails/{registrationId}/{medicalRecordId}")
    public JSONObject getAllPaymentDetails(@PathVariable("registrationId") Integer registrationId,@PathVariable("medicalRecordId") Integer medicalRecordId,Authentication authentication){
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        return doctorService.getAllPaymentDetails(medicalRecordId,registrationId);
    }

    @PostMapping("/getDoctorTotal")
    public JSONObject getDoctorTotal(@RequestBody JSONObject object,Authentication authentication){
        Integer doctorId = null;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        }catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }

        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Date start= null,end=null;
        try {
            start =  ft.parse(object.get("start").toString());
            end=  ft.parse(object.get("end").toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_804);
        }
        return doctorService.getDoctorTotal(doctorId,object.get("start").toString(),object.get("end").toString());
    }

    private JSONObject checkTemplate(String templateType, Integer doctorId, String name, Integer level) {
        //检查权限  主任医师-院级 副主任医生-科级 主治医生-个人
        if (name == null || name.equals("")) {
            return CommonUtil.errorJson(ErrorEnum.E_702.addErrorParamName(templateType));
        }
        try {
            if (level.equals(Constants.HOSPITALLEVEL)) {
                isChiefDoctor(doctorId);
            } else if (level.equals(Constants.DEPTLEVEL)) {
                aboveDeputyChiefDoctor(doctorId);
            } else if (level.equals(Constants.PERSONALLEVEL)) {
                aboveATTENDING_DOCTOR(doctorId);
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("noSuchLevel"));
            }
        } catch (AuthenticationServiceException ex) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(ex.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("level"));
        }
        return null;
    }

}

