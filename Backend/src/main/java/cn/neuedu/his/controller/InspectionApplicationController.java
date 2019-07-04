package cn.neuedu.his.controller;

import cn.neuedu.his.config.FastDFS.FastDFSFile;
import cn.neuedu.his.config.FastDFS.FileManager;
import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.csource.common.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    InspectionResultService inspectionResultService;

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
     *
     * @param name
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping({"/selectPatientInformationByNameOrId/name/{name}", "/selectPatientInformationByNameOrId/id/{id}",
            "/selectPatientInformationByNameOrId/nameAndId/{name}/{id}", "/selectPatientInformationByNameOrId"})
    JSONObject selectPatientInformationByNameOrId(@PathVariable(value = "name", required = false) String name, @PathVariable(value = "id", required = false) Integer id, Authentication authentication) {

        Boolean auth = null;
        Integer departmentId = null;
        //判断权限
        try {
            PermissionCheck.isHosptialAdimReturnUserId(authentication);
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

    @PostMapping("/confirmApplication/{id}")
    public JSONObject confirmApplication(@PathVariable("id") Integer id, Authentication authentication) {

        try {
            PermissionCheck.isTechnicalDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_632);
        }

        try {
            inspectionApplicationService.confirmApplication(id);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("634"))
                return CommonUtil.errorJson(ErrorEnum.E_634);
        }
        return CommonUtil.successJson();
    }

    @PostMapping("/cancelApplication/{id}")
    public JSONObject cancelApplication(@PathVariable("id") Integer id, Authentication authentication) {

        try {
            PermissionCheck.isTechnicalDoctor(authentication);
            inspectionApplicationService.cancelApplication(id);
            return CommonUtil.successJson();
        } catch (RuntimeException e) {
            return CommonUtil.errorJson(ErrorEnum.E_635);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_632);
        }

    }

    @PostMapping("/entryApplicationResult")
    public JSONObject entryApplicationResult(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.isTechnicalDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        //判断权限
        Integer id = (Integer) data.get("id");

        InspectionResult inspectionResult = JSONObject.toJavaObject(jsonObject, InspectionResult.class);
        inspectionResult.setOperatorId(id);

        inspectionApplicationService.entryApplicationResult(inspectionResult);
        return CommonUtil.successJson();
    }


    @PostMapping("/upload")
    public JSONObject upload(@RequestPart("pic") MultipartFile pic) {
        // 获取文件后缀名
        String ext = "jpg";
        try (InputStream fileReader = pic.getInputStream()) {
            byte[] bytes = new byte[10000000];//10M
            int length = fileReader.read(bytes);
            FastDFSFile file = new FastDFSFile(bytes, ext);
            NameValuePair[] meta_list = new NameValuePair[4];
            //设置文件相关属性
            meta_list[0] = new NameValuePair("fileName", pic.getOriginalFilename());
            meta_list[1] = new NameValuePair("fileLength", String.valueOf(length));
            meta_list[2] = new NameValuePair("fileExt", ext);
            meta_list[3] = new NameValuePair("fileAuthor", "CCM");
            String filePath = FileManager.upload(file, meta_list);
            return CommonUtil.successJson(filePath);
        } catch (RuntimeException e) {
            return CommonUtil.errorJson(ErrorEnum.E_640);
        } catch (IOException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
    }
}
