package cn.neuedu.his.controller;

import cn.neuedu.his.model.*;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.InspectionApplicationService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/inspection_application")
public class InspectionApplicationController {

    @Autowired
    InspectionApplicationService inspectionApplicationService;
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;

    /**
     * 暂存检查/处置
     *
     * @param object
     * @return
     */
    @PostMapping("/saveTemporaryInspection")
    public JSONObject saveTemporaryInspection(@RequestBody JSONObject object) {
        Integer id = Integer.parseInt(object.get("registrationId").toString());
        List<InspectionApplication> record = JSONObject.parseArray(object.get("inspections").toString(), InspectionApplication.class);
        try {
            redisService.setTemporaryInspection(id, record);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_801);
        }
        return CommonUtil.successJson();
    }

    @PostMapping("/saveTemporaryInspectionDrug")
    public JSONObject saveTemporaryInspectionDrug(@RequestBody JSONObject object) {
        Integer id = Integer.parseInt(object.get("registrationId").toString());
        List<Prescription> prescriptions = JSONObject.parseArray(object.getString("prescriptions"), Prescription.class);
        try {
            redisService.setTemporaryInspectionDrug(id, prescriptions);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_801);
        }
        return CommonUtil.successJson();
    }


    /**
     * 获得暂存检查/处置
     *
     * @param registrationId
     * @return
     */
    @GetMapping("/getTemporaryInspection/{registrationId}")
    public JSONObject getTemporaryInspection(@PathVariable("registrationId") Integer registrationId) {
        JSONObject object = new JSONObject();
        try {
            List<Prescription> prescriptions = redisService.getTemporaryPrescription(registrationId);
            if (prescriptions == null)
                prescriptions = new ArrayList<>();
            object.put("prescriptions", prescriptions);
            List<InspectionApplication> applications = redisService.getTemporaryApplications(registrationId);
            if (applications == null)
                applications = new ArrayList<>();
            object.put("applications", applications);
            return CommonUtil.successJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    /**
     * 删除暂存
     *
     * @param registrationId
     * @return
     */
    @GetMapping("/deleteTemporaryInspection/{registrationId}")
    public JSONObject deleteTemporaryInspection(@PathVariable("registrationId") Integer registrationId) {
        try {
            redisService.deleteTemporaryInspection(registrationId);
            return CommonUtil.successJson();
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_803);
        }
    }

    /**
     * 模糊查询信息（病历号和名字）
     * 获得所有信息
     * @param name
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping({"/selectPatientInformationByNameOrId/name/{name}", "/selectPatientInformationByNameOrId/id/{id}",
            "/selectPatientInformationByNameOrId/nameAndId/{name}/{id}", "/selectPatientInformationByNameOrId"})
    JSONObject selectPatientInformationByNameOrId(@PathVariable(value = "name", required = false) String name, @PathVariable(value = "id", required = false) Integer id, Authentication authentication) {

        Boolean auth;
        Integer departmentId = null;
        //判断权限
        try {
            PermissionCheck.isHosptialAdimReturnUserId(authentication);
            auth = true;
        } catch (Exception e) {
            auth = false;

            //设定部门ID
            Integer userId = Integer.valueOf(e.getMessage());
            departmentId = userService.findById(userId).getDepartmentId();
        }


        List<Payment> payments = inspectionApplicationService.selectPatientInformationByNameOrId(name, id, departmentId, auth);

        return CommonUtil.successJson(payments);
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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
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
     * 取消申请
     * @param id
     * @param authentication
     * @return
     */
    @PostMapping("/cancelInspectionApplication/{id}")
    JSONObject cancelInspectionApplication(@PathVariable(value = "id",required = false) Integer id, Authentication authentication){

        //判断权限（请求者是不是该部门的人）
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer doctorId = (Integer) data.get("id");

        User user = userService.findById(doctorId);
        InspectionApplication inspectionApplication = inspectionApplicationService.findById(id);

        inspectionApplication.setCanceled(true);
        inspectionApplicationService.update(inspectionApplication);

        return CommonUtil.successJson();
    }


    @PostMapping("confirmApplication/{id}")
    JSONObject confirmApplication(JSONObject jsonObject) {

        return CommonUtil.successJson();
    }
}
