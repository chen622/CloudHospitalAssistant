package cn.neuedu.his.controller;

import cn.neuedu.his.mapper.MedicalRecordMapper;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.RegistrationService;
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
    MedicalRecordMapper medicalRecordMapper;

    @GetMapping("/getAllRecord/{patientID}")
    public JSONObject getAllRecordByPatientId(@PathVariable("patientID") Integer patientID, Authentication authentication)
    throws AuthenticationServiceException {
        PermissionCheck.isOutpatientDoctor(authentication);
        return CommonUtil.successJson(medicalRecordMapper.getAllByPatientId(patientID));
    }

    @GetMapping("/getByName")
    public JSONObject getRegistrationByPatientName(@RequestBody String name,Authentication authentication )
    throws AuthenticationServiceException{
        CommonUtil.successJson(authentication);
        List<Registration> list=registrationService.getRegistrationByPatientName(name);
        if (list==null){
            list=new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }

    /**
     * update the registration state as first diagnose which is 48
     * @param id
     * @return
     */
    @PostMapping("/update")
    public JSONObject updateStateToOne(@RequestBody Integer id,Authentication authentication)
    throws  AuthenticationServiceException{
        PermissionCheck.isOutpatientDoctor(authentication);
        Registration registration = registrationService.findById(id);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registration id"));
        } else{
            registration.setDoctorId(Constants.finalDiagnosis);
            registrationService.update(registration);
            return CommonUtil.successJson();
        }
    }

}
