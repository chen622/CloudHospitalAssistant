package cn.neuedu.his.controller;

import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.PrescriptionService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ccm on 2019/05/29.
 */
@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    PrescriptionService prescriptionService;
    @Autowired
    DoctorService doctorService;


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
            try {
                doctorId = PermissionCheck.isTechnicalDoctor(authentication);
            } catch (AuthenticationServiceException aa) {
                return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
            } catch (Exception e) {
                return CommonUtil.errorJson(ErrorEnum.E_802);
            }
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        Integer registrationId = Integer.parseInt(object.get("registrationId").toString());

        List<Prescription> prescriptions = JSONObject.parseArray(object.get("prescriptions").toString(), Prescription.class);
        if (prescriptions != null && !prescriptions.isEmpty()) {
            try {
                return doctorService.savePrescriptions(prescriptions, registrationId, doctorId);
            } catch (Exception e) {
                return CommonUtil.errorJson(ErrorEnum.E_500.addErrorParamName(e.getMessage()));
            }
        } else {
            return CommonUtil.errorJson(ErrorEnum.E_704);
        }
    }
}
