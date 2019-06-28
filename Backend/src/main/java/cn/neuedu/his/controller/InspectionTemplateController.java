package cn.neuedu.his.controller;

import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.InspectionTemplate;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.InspectionTemplateService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/IT")
@Import(FeignClientsConfiguration.class)
public class InspectionTemplateController {

    @Autowired
    InspectionTemplateService inspectionTemplateService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    UserService userService;

    @GetMapping("/getIT")
    public JSONObject getIT(Authentication authentication) {
        try {
            Integer doctorId = PermissionCheck.isOutpatientDoctor(authentication);
            User user = userService.findById(doctorId);
            if (user == null) {
                return CommonUtil.errorJson(ErrorEnum.E_502);
            }
            return CommonUtil.successJson(inspectionTemplateService.getWhichICanUse(user.getId(),user.getDepartmentId()));
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
    }


    //TODO cloud
    /**
     * 保存检查模板
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/saveIT")
    public JSONObject saveInspectionTem(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        InspectionTemplate template = JSONObject.toJavaObject(object, InspectionTemplate.class);
//        JSONObject k = checkTemplate("inspection", doctorId, template.getName(), template.getLevel());
//        if (k != null)
//            return k;
        try {
            return doctorService.saveInspectionAsTemplate(template, doctorId);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }
    }


//    /**
//     * 门诊医生查看全院检查模板
//     *
//     * @param authentication
//     * @return
//     */
//    @GetMapping("/getHospitalCheckTemps")
//    public JSONObject getHospitalCheckTemps(Authentication authentication) {
//        try {
//            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
//            return doctorService.getHospitalCheckTemps(doctorID, Constants.HOSPITALLEVEL);
//        } catch (AuthenticationServiceException a) {
//            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//    }
//
//    /**
//     * 门诊医生查看所在科室检查模板
//     *
//     * @param authentication
//     * @return
//     */
//    @GetMapping("/getDeptCheckTemps")
//    public JSONObject getDeptCheckTemps(Authentication authentication) {
//        try {
//            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
//            return (doctorService.getDeptCheckTemps(doctorID, Constants.DEPTLEVEL));
//        } catch (AuthenticationServiceException a) {
//            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//    }
//
//    /**
//     * 门诊医生查看个人检查模板
//     *
//     * @param authentication
//     * @return
//     */
//    @GetMapping("/getPersonalCheckTemps")
//    public JSONObject getPersonalInspectionTemps(Authentication authentication) {
//        try {
//            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
//            JSONObject object1 = (doctorService.getPersonalCheckTemps(doctorID, Constants.PERSONALLEVEL));
//            return object1;
//        } catch (AuthenticationServiceException a) {
//            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//    }

    /**
     * 通过名字查找检查模板
     *
     * @param name
     * @param authentication
     * @return
     */
    @GetMapping("/getInspectionTemByName/{name}")
    public JSONObject getInspectionTemByName(@PathVariable("name") String name, Authentication authentication) {
        try {
            Integer doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            return doctorService.getInspectionTemByName(name);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }


    /**
     * 更新检查模板
     *
     * @param object
     * @param authentication
     * @return
     */
    @PostMapping("/updateInspectionTem")
    public JSONObject updateInspectionTem(@RequestBody JSONObject object, Authentication authentication) {
        Integer doctorId;
        try {
            doctorId = PermissionCheck.isOutpatientDoctor(authentication);
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName("OutpatientDoctor"));
        }
        InspectionTemplate template = JSONObject.parseObject(object.get("template").toString(), InspectionTemplate.class);
        Integer crearedById = template.getCreatedById();
        if (!crearedById.equals(doctorId))
            return CommonUtil.errorJson(ErrorEnum.E_706.addErrorParamName("InspectionTemplate"));

//        JSONObject k = checkTemplate("inspection", doctorId, template.getName(), template.getLevel());
//        if (k != null)
//            return k;
        try {
            return doctorService.updateInspectionTem(template, doctorId);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }
    }

    @GetMapping("/deleteIT/{id}")
    public JSONObject deleteInspectionTemp(@PathVariable("id") Integer id, Authentication authentication) {
        Integer doctorID = null;
        try {
            doctorID = PermissionCheck.isOutpatientDoctor(authentication);
            PermissionCheck.aboveATTENDING_DOCTOR(doctorService.findById(doctorID).getTitleId());
        } catch (AuthenticationServiceException a) {
            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        return doctorService.deleteInspectionTemp(id, doctorID);
    }
}
