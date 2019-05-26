package cn.neuedu.his.controller;

import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.service.impl.RegistrationServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @GetMapping("/getAllWait")
    public JSONObject getAllWaitingRegistration(){
        System.out.println("****====="+registrationService);
        return CommonUtil.successJson(registrationService.getAllWaitingRegistration());
//        return (registrationService.getAllWaitingRegistration());
    }


    @GetMapping("/getByName")
    public JSONObject getRegistrationByPatientName(@RequestBody String name ){
        return CommonUtil.successJson(registrationService.getRegistrationByPatientName(name));
    }

    /**
     * update the registration state as first diagnose which is 48
     * @param id
     * @return
     */
    @PostMapping("/update")
    public JSONObject updateStateToOne(@RequestBody Integer id){
        Registration registration = registrationService.findById(id);
        registration.setDoctorId(48);
        return CommonUtil.successJson(registrationService.update(registration));
    }

}
