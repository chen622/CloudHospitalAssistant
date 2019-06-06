package cn.neuedu.his.controller;

import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    /**
     * 现场挂号
     * @param jsonObject
     * @return
     */
    @PostMapping("/registration")
    public JSONObject registration(@RequestBody JSONObject jsonObject, Authentication authentication) {
        //获取挂号收费员id
        Integer registrarId = null;
        try {
            registrarId = PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            registrationService.registerRegistrationInfo(registrarId, jsonObject.getInteger("patientId"), jsonObject.getInteger("scheduleId"), jsonObject.getBoolean("needBook"));
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }catch (IndexOutOfBoundsException e1) {
            return CommonUtil.errorJson(ErrorEnum.E_510);
        }

        return CommonUtil.successJson();
    }

    /**
     * 退号
     * @param registrationId
     * @param authentication
     * @return
     */
    @PostMapping("/retreat/{registrationId}")
    public JSONObject retreatRegistration(@PathVariable("registrationId") Integer registrationId, Authentication authentication) {
        Integer registrarId=null;
        try {
            registrarId = PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            registrationService.retreatRegistrationInfo(registrationId, registrarId);
        }catch (UnsupportedOperationException e) {
            if (e.getMessage().equals("redis"))
                return CommonUtil.errorJson(ErrorEnum.E_511);
            return CommonUtil.errorJson(ErrorEnum.E_503);
        }catch (IllegalArgumentException e2) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e2.getMessage()));
        }
        return CommonUtil.successJson();
    }

    /**
     * 通过病历号查询未就诊或已预约挂号信息
     * @param patientId
     * @return
     */
    @GetMapping("/getWaitingRegistration/{patientId}")
    public JSONObject getWaitingRegistration(@PathVariable("patientId") Integer patientId) {
        JSONArray result = new JSONArray();
        ArrayList<Registration> registrationArrayList = registrationService.findAllRegistrationWaitingByPatientId(patientId);
        if (registrationArrayList.isEmpty())
            return CommonUtil.successJson(null);

        for (Registration registration: registrationArrayList) {
            result.add(registration);
        }

        return CommonUtil.successJson(result);
    }

}
