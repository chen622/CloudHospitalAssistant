package cn.neuedu.his.controller;

import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
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
        Integer registrarId;
        try {
            registrarId = PermissionCheck.getIdByRegistrar(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        try {
            registrationService.registerRegistrationInfo(registrarId, jsonObject);
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
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
        Integer registrarId;
        try {
            registrarId = PermissionCheck.getIdByRegistrar(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        try {
            registrationService.retreatRegistrationInfo(registrationId, registrarId);
        }catch (UnsupportedOperationException e) {
            if (e.getMessage().equals("503"))
                return CommonUtil.errorJson(ErrorEnum.E_503);
            else if (e.getMessage().equals("504"))
                return CommonUtil.errorJson(ErrorEnum.E_504);
            else if (e.getMessage().equals("505"))
                return CommonUtil.errorJson(ErrorEnum.E_505);
        }
        return CommonUtil.successJson();
    }

}
