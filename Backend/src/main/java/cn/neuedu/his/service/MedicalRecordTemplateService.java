package cn.neuedu.his.service;

import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by ccm on 2019/05/24.
 */
public interface MedicalRecordTemplateService extends Service<MedicalRecordTemplate> {
    List<MedicalRecordTemplate> getHospitalMRByLevel(Integer doctorID, Integer level);

    List<MedicalRecordTemplate> getWhichICanUse(Integer doctorId, Integer departmentId);

    List<MedicalRecordTemplate> getDeptMRByLevel(Integer doctorID, Integer level);

    List<MedicalRecordTemplate> getPersonalMR(Integer doctorID, Integer level);

    List<MedicalRecordTemplate> getMedicalRecordTemByName(String name);
}

