package cn.neuedu.his.controller;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * Created by ccm on 2019/05/26.
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping("/registerPatient")
    public JSONObject registerPatient(@RequestBody JSONObject jsonObject) {
        Patient patient = jsonObject.getJSONObject("Patient").toJavaObject(Patient.class);
        patient.setCreateTime(new Date(System.currentTimeMillis()));
        patientService.save(patient);
        return CommonUtil.successJson();
    }

    /**
     * 查询病患个人信息及所有未缴费信息
     * @param patientId
     * @return
     */
    @GetMapping("/getUnpaidPayment/{patientId}")
    public JSONObject getUnpaidPaymentAndPatient(@PathVariable("patientId") Integer patientId, Authentication authentication) {
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        JSONObject result = new JSONObject();
        Patient patient;
        try {
            patient = patientService.findPatientAndPaymentInfo(patientId);
        }catch (IllegalArgumentException e) {
           return  CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        result.put("patient", patient);
        BigDecimal totalAmount = new BigDecimal(0);
        for(Payment payment: patient.getPaymentList()) {
            totalAmount = totalAmount.add(payment.getUnitPrice().multiply(new BigDecimal(payment.getQuantity())));
        }
        result.put("totalAmount", totalAmount);
        return CommonUtil.successJson(result);
    }

    /**
     * 查看患者信息及其项目缴费信息
     * @param patientId
     * @param authentication
     * @return
     */
    @GetMapping("/getUnConsumePayment/{patientId}")
    public JSONObject getNotConsumePaymentAndPatient(@PathVariable("patientId") Integer patientId, Authentication authentication) {
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        Patient patient;
        try {
            patient = patientService.findPatientAndNotConsumePayment(patientId);
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson(patient);
    }

    /**
     * 获取患者需要取的药物信息
     * @param patientId
     * @param authentication
     * @return
     */
    @GetMapping("/getDrugTaken/{patientId}")
    public JSONObject getDrugTakenInfo(@PathVariable("patientId") Integer patientId, Authentication authentication) {
        try {
            PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        Patient patient;
        try {
            patient = patientService.findPatientAndNotTakeDrug(patientId);
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson(patient);
    }

    /**
     * 获取患者某一时期药物信息
     * @param jsonObject
     * @param authentication
     * @return
     */
    @GetMapping("/getDrugDuringDate")
    public JSONObject getDrugDuringDateInfo(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        Patient patient;
        try {
            patient = patientService.findPatientAndDrugDuringDate(jsonObject.getInteger("patientId"), jsonObject.getDate("startDate"), jsonObject.getDate("endDate"));
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson(patient);
    }
}
