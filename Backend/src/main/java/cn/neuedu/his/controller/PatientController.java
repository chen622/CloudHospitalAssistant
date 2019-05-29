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

}
