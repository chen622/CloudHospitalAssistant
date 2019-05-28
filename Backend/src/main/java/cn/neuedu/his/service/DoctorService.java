package cn.neuedu.his.service;
import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.InspectionTemplate;
import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DoctorService extends Service<Doctor> {

    @Transactional
    public JSONObject setFirstDiagnose(Integer registrationID, MedicalRecord medicalRecord) throws Exception;

    @Transactional
    public JSONObject getHospitalCheckTemps(Integer registrationId,Integer doctorID,Integer level);

}
