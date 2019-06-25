package cn.neuedu.his.controller;

import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.StringUtils;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
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
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private InspectionApplicationService inspectionApplicationService;


    @GetMapping("/getByDepartmentId/{id}")
    public JSONObject getByDepartmentId(@PathVariable("id") Integer departmentId) {
        List<Doctor> doctors = doctorService.getByDepartmentId(departmentId);
        if (doctors == null) {
            doctors = new ArrayList<>();
        }
        return CommonUtil.successJson(doctors);
    }


    @GetMapping("/getPrescriptionAndInspection/{registrationId}")
    public JSONObject getAllByMedical(@PathVariable("registrationId") Integer registrationId, Authentication authentication) {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        if (registrationId == null) {
            return CommonUtil.errorJson(ErrorEnum.E_501);
        }
        MedicalRecord medicalRecord = medicalRecordService.getByRegistrationId(registrationId);
        if (medicalRecord == null) {
            return CommonUtil.errorJson(ErrorEnum.E_501);
        }
        JSONObject result = new JSONObject();
        result.put("prescriptions", prescriptionService.findPrescriptionAndDrugByMedical(medicalRecord.getId()));
        result.put("inspections", inspectionApplicationService.findAllByMedical(medicalRecord.getId()));

        return CommonUtil.successJson(result);
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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        List<Registration> list = registrationService.getRegistrationByPatientName(name, doctorID, Constants.WAITING_FOR_TREATMENT);
        if (list == null) {
            list = new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    @GetMapping("/getRegistrationInfo/{time}")
    public JSONObject getRegistrationInfo(@PathVariable("time") Date time, Authentication authentication) {
        Integer doctorID;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        return doctorService.getRegistrationInof(time, doctorID);
    }

    /**
     * 找到该医生当天对应的所有待诊挂号
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getAllRegistration")
    public JSONObject getAllRegistration(Authentication authentication) {
        Date time = new Date(System.currentTimeMillis());
        Integer doctorID;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        List<Registration> list = registrationService.getAllWaitingRegistration(doctorID, Constants.WAITING_FOR_TREATMENT, time);
        List<Registration> inside = registrationService.getAllWaitingRegistration(doctorID, Constants.INSIDE_DOCTOR, time);
        if (list == null) {
            list = new ArrayList<>();
            if (inside != null) {
                list.addAll(inside);
            }
        }
        List<Registration> list2 = registrationService.getAllWaitingRegistration(doctorID, Constants.FIRST_DIAGNOSIS, time);
        if (list2 == null)
            list2 = new ArrayList<>();
        List<Registration> te = registrationService.getAllWaitingRegistration(doctorID, Constants.SUSPECT, time);
        if (te != null)
            list2.addAll(te);
        te = registrationService.getAllWaitingRegistration(doctorID, Constants.FINAL_DIAGNOSIS, time);
        if (te != null)
            list2.addAll(te);

        List<Registration> finish = registrationService.getAllWaitingRegistration(doctorID, Constants.FINISH_DIAGNOSIS, time);
        if (finish == null)
            finish = new ArrayList<>();
        JSONObject map = new JSONObject();

        map.put("wait", StringUtils.setAgeForRegistrationArray(list));
        map.put("in", StringUtils.setAgeForRegistrationArray(list2));
        map.put("out", StringUtils.setAgeForRegistrationArray(finish));
        return CommonUtil.successJson(map);
    }

    @GetMapping("/insideDoc/{registrationId}")
    public JSONObject getInsideDoc(@PathVariable("registrationId") Integer registrationId, Authentication authentication) {
        Integer doctorID;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        Registration r = registrationService.findById(registrationId);
        if (r == null || !r.getState().equals(Constants.WAITING_FOR_TREATMENT)) {
            return CommonUtil.errorJson(ErrorEnum.E_705);
        }
        r.setState(Constants.INSIDE_DOCTOR);
        registrationService.update(r);
        return CommonUtil.successJson();
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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    @GetMapping("/getAllDiseases")
    public JSONObject getAllDiseases(Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return CommonUtil.successJson(doctorService.getAllDiease());
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }


    //PARTTWO-检查/检验/申请
//    @GetMapping("/openInspection")
//    public JSONObject openInspection(JSONObject object, Authentication authentication) {
//        Integer doctorID = null;
//        try {
//            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
//        } catch (AuthenticationServiceException a) {
//            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//        try {
//            Integer registrationId = Integer.parseInt(object.get("registrationId").toString());
//            return doctorService.openInspection(registrationId);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
//        }
//    }

    //TODO cloud
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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        return doctorService.getInspectionResult(id);
    }


    //part three


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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
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
                e.printStackTrace();
                return CommonUtil.errorJson(ErrorEnum.E_500.addErrorParamName(e.getMessage()));
            }
        } else {
            return CommonUtil.errorJson(ErrorEnum.E_704);
        }
    }

    @GetMapping("/getPrescriptionsTemByName/{name}")
    public JSONObject getPrescriptionsTemByName(@PathVariable("name") String name, Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return doctorService.getPrescriptionsTemByName(name);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    @GetMapping("/finishDiagnose/{registrationId}")
    public JSONObject finishDiagnose(@PathVariable("registrationId") Integer registrationId, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        return doctorService.finishDiagnose(registrationId);
    }

    @GetMapping("/paymentDetails/{registrationId}/{medicalRecordId}")
    public JSONObject getAllPaymentDetails(@PathVariable("registrationId") Integer registrationId, @PathVariable("medicalRecordId") Integer medicalRecordId, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        return doctorService.getAllPaymentDetails(medicalRecordId, registrationId);
    }

    @PostMapping("/getDoctorTotal")
    public JSONObject getDoctorTotal(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId = null;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date start = null, end = null;
        try {
            start = ft.parse(object.get("start").toString());
            end = ft.parse(object.get("end").toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_804);
        }
        return doctorService.getDoctorTotal(doctorId, object.get("start").toString(), object.get("end").toString());
    }

    private JSONObject checkTemplate(String templateType, Integer doctorId, String name, Integer level) {
        //检查权限  主任医师-院级 副主任医生-科级 主治医生-个人
        if (name == null || name.equals("")) {
            return CommonUtil.errorJson(ErrorEnum.E_702.addErrorParamName(templateType));
        }
        try {
            Integer titleId = doctorService.findById(doctorId).getTitleId();
            if (level.equals(Constants.HOSPITALLEVEL)) {
                PermissionCheck.isChiefDoctor(titleId);
            } else if (level.equals(Constants.DEPTLEVEL)) {
                PermissionCheck.aboveDeputyChiefDoctor(titleId);
            } else if (level.equals(Constants.PERSONALLEVEL)) {
                PermissionCheck.aboveATTENDING_DOCTOR(titleId);
            } else {
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("noSuchLevel"));
            }
        } catch (AuthenticationServiceException ex) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("level"));
        }
        return null;
    }

    /**
     * 计算门诊医生工作量
     *
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/getDoctorWorkload")
    public JSONObject getDoctorWorkload(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.isFinancialOfficer(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        Date end = jsonObject.getDate("end");
        if (end == null)
            end = new Date(System.currentTimeMillis());

        return CommonUtil.successJson(doctorService.doctorWorkCalculate(jsonObject.getDate("start"), end));
    }

    @PostMapping("/getDoctorStatistics")
    public JSONObject getDoctorStatistics(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            try {
                doctorId = PermissionCheck.isTechnicalDoctor(authentication);
            } catch (Exception ex) {
                return CommonUtil.errorJson(ErrorEnum.E_502);
            }
        }

        String start = (String) object.get("start");
        String end = (String) object.get("end");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
        if (start == null || start.equals("")) {
            start = format.format(System.currentTimeMillis());                                //获得带格式的字符串
        }
        if (end == null || end.equals("")) {
            end = format.format(System.currentTimeMillis());
        }
        return doctorService.getDoctorStatistics(doctorId, start, end);
    }

    @PostMapping("/getRStatistics")
    public JSONObject getRStatistics(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            try {
                doctorId = PermissionCheck.isTechnicalDoctor(authentication);
            } catch (Exception ex) {
                return CommonUtil.errorJson(ErrorEnum.E_502);
            }
        }
        String start = (String) object.get("start");
        String end = (String) object.get("end");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
        if (start == null || start.equals("")) {
            start = format.format(System.currentTimeMillis());                                //获得带格式的字符串
        }
        if (end == null || end.equals("")) {
            end = format.format(System.currentTimeMillis());
        }
        return doctorService.getRegistrationStatistics(doctorId, start, end);
    }
}

