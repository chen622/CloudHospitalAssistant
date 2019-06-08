package cn.neuedu.his.controller;

import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.service.MedicalRecordService;
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

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/medical_record")
public class MedicalRecordController {

    @Autowired
    MedicalRecordService medicalRecordService;
    @Autowired
    RedisServiceImpl redisService;

    /**
     * 暂存病历
     *
     * @param object
     * @return
     */
    @PostMapping("/saveTemporaryMR")
    public JSONObject saveTemporaryMR(@RequestBody JSONObject object) {
        Integer id = Integer.parseInt(object.get("registrationId").toString());
        MedicalRecord record = JSONObject.parseObject(object.get("record").toString(), MedicalRecord.class);
        try {
            redisService.setTemporaryMedicalRecord(id, record);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_801);
        }
        return CommonUtil.successJson();
    }

    /**
     * 获得暂存病历
     *
     * @param registrationId
     * @return
     */
    @GetMapping("/getTemporaryMR/{registrationId}")
    public JSONObject getTemporaryMR(@PathVariable("registrationId") Integer registrationId) {
        try {
            MedicalRecord record = redisService.getTemporaryMedicalRecord(registrationId);
            if (record == null)
                record = new MedicalRecord();
            return CommonUtil.successJson(record);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    /**
     * 删除暂存病历
     *
     * @param registrationId
     * @return
     */
    @GetMapping("/deleteTemporaryMR/{registrationId}")
    public JSONObject deleteTemporaryMR(@PathVariable("registrationId") Integer registrationId) {
        try {
            redisService.deleteTemporaryMR(registrationId);
            return CommonUtil.successJson();
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_803);
        }
    }

    /**
     * 通过患者id获得该患者所有的病历
     *
     * @param patientID
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    @GetMapping("/getAllRecord/{patientID}")
    public JSONObject getAllRecordByPatientId(@PathVariable("patientID") Integer patientID, Authentication authentication) {
        try {
            PermissionCheck.isOutpatientDoctor(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        List<MedicalRecord> list = medicalRecordService.getAllByPatientId(patientID);
        if (list == null) {
            list = new ArrayList<>();
        }
        return CommonUtil.successJson(list);
    }
}
