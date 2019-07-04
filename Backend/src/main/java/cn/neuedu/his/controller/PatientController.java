package cn.neuedu.his.controller;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.StringUtils;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by ccm on 2019/05/26.
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;
    @Autowired
    RegistrationService registrationService;

    @PostMapping("/add")
    public JSONObject registerPatient(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
            Patient patient = jsonObject.toJavaObject(Patient.class);
            List<Patient> patients = patientService.selectPatientByIdentifyId(patient.getIdentityId());
            if (!(patients == null || patients.size() == 0)) {
                return CommonUtil.errorJson(ErrorEnum.E_639);
            }
            patient.setConfirm(true);
            patientService.save(patient);
            return CommonUtil.successJson();
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
    }

    /**
     * 患者(某段时间内)所有缴费单信息
     *
     * @param jsonObject
     * @param authentication
     * @return
     */
    @GetMapping("/getAllPayment")
    public JSONObject getAllPayment(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        Patient patient = patientService.findAllPayment(jsonObject.getInteger("patientId"), jsonObject.getDate("startDate"), jsonObject.getDate("endDate"));

        return CommonUtil.successJson(patient);
    }

    /**
     * 查询病患个人信息及所有未缴费信息
     *
     * @param patientId
     * @return
     */
    @GetMapping("/getNotPaidPayment/{patientId}")
    public JSONObject getNotPaidPayment(@PathVariable("patientId") Integer patientId, Authentication authentication) {
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        JSONObject result = new JSONObject();
        Patient patient;
        try {
            patient = patientService.findNotPaidPayment(patientId);
        } catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        result.put("patient", patient);
        BigDecimal totalAmount = new BigDecimal(0);
        for (Payment payment : patient.getPaymentList()) {
            totalAmount = totalAmount.add(payment.getUnitPrice().multiply(new BigDecimal(payment.getQuantity())));
        }
        result.put("totalAmount", totalAmount);
        return CommonUtil.successJson(result);
    }

    /**
     * 查看患者信息及其项目缴费信息
     *
     * @param patientId
     * @param authentication
     * @return
     */
    @GetMapping("/getNotConsumePayment/{patientId}")
    public JSONObject getNotConsumePaymentAndPatient(@PathVariable("patientId") Integer patientId, Authentication authentication) {
//        try {
//            PermissionCheck.getIdByPaymentAdmin(authentication);
//        } catch (AuthenticationServiceException e) {
//            return CommonUtil.errorJson(ErrorEnum.E_502);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }

        Patient patient;
        try {
            patient = patientService.findNotConsumePayment(patientId);
        } catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson(patient);
    }

    /**
     * 获取患者需要取的药物信息
     *
     * @param authentication
     * @return
     */
    @PostMapping("/getDrug")
    public JSONObject getDrugNotTakeInfo(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.getIdByDrugAdmin(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        Date start = jsonObject.getDate("start");
        Date end = jsonObject.getDate("end");

        if (start == null)
            end = null;
        else if (end == null)
            end = new Date(System.currentTimeMillis());

        Patient patient;
        JSONObject result;
        try {
            patient = patientService.findPatientAndNotTakeDrug(jsonObject.getInteger("patientId"), start, end);
            result = patientService.findPatientAndAllDrugInfo(jsonObject.getInteger("patientId"), start, end);
        } catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }
        JSONObject re = new JSONObject();
        re.put("notTake", patient);

        re.put("token", result);
        return CommonUtil.successJson(re);
    }

    /**
     * 获取患者退药信息
     *
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/retreatDrug")
    public JSONObject getDrugRetreatInfo(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.getIdByDrugAdmin(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        Date start = jsonObject.getDate("start");
        Date end = jsonObject.getDate("end");

        if (start == null)
            end = null;
        else if (end == null)
            end = new Date(System.currentTimeMillis());

        JSONObject result;

        try {
            result = patientService.findPatientAndAllDrugInfo(jsonObject.getInteger("patientId"), start, end);
        } catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson(result);
    }

    @GetMapping("/getAll")
    public JSONObject findAll() {
        List<Patient> patients = patientService.selectPatientByIdentifyId("");
        try {
            return CommonUtil.successJson(StringUtils.setAgeForPatientArray(patients));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    /**
     *
     */
    @PostMapping("/searchByMulti")
    public JSONObject selectPatientByIdentifyId(@RequestBody JSONObject json) {
        String identifyId = json.getString("id");
        String name = json.getString("realName");
        String phone = json.getString("phone");
        identifyId = identifyId == null ? "" : identifyId;
        name = name == null ? "" : name;
        phone = phone == null ? "" : phone;
        List<Patient> patients = patientService.selectPatientByIdentifyIdAndNameAndPhone(identifyId, name, phone);

        JSONArray jsonArray = null;
        try {
            jsonArray = StringUtils.setAgeForPatientArray(patients);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }

        return CommonUtil.successJson(jsonArray);
    }

    /**
     * 获得患者所有挂号信息
     * @param authentication
     * @return
     */
    @GetMapping("/getRegistrations")
    public JSONObject getRegistrations(Authentication authentication){
       try {
           Integer id=PermissionCheck.getIdByPatient(authentication);
           return CommonUtil.successJson(registrationService.getRegistrations(id));
       }catch (AuthenticationServiceException e){
           return CommonUtil.errorJson(ErrorEnum.E_602);
       }

    }
//    /**
//     * 根据真实姓名获得病人信息
//     *
//     * @param name
//     * @return
//     */
//    @GetMapping("/getByName/{name}")
//    public JSONObject selectPatientByName(@PathVariable("name") String name) {
//        if (name == null)
//            name = "";
//
//
//        List<Patient> patients = patientService.selectPatientByName(name);
//
//        JSONArray jsonArray = setAge(patients);
//
//        return CommonUtil.successJson(jsonArray);
//    }
//
//    /**
//     * 根据电话号获得病人信息
//     *
//     * @param phoneNumber
//     * @return
//     */
//    @GetMapping("/getByPhone/{phoneNumber}")
//    public JSONObject selectPatientByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
//        if (phoneNumber == null)
//            phoneNumber = "";
//
//
//        List<Patient> patients = patientService.selectPatientByPhone(phoneNumber);
//
//        JSONArray jsonArray = setAge(patients);
//
//        return CommonUtil.successJson(jsonArray);
//    }

}
