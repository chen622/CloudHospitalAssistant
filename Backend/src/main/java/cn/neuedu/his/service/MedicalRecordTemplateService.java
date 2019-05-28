package cn.neuedu.his.service;
import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface MedicalRecordTemplateService extends Service<MedicalRecordTemplate> {
    public List<MedicalRecordTemplate> getHospitalMR(Integer doctorID, Integer level) ;

    public List<MedicalRecordTemplate> getDeptMR(Integer doctorID,Integer level) ;

    public List<MedicalRecordTemplate> getPersonalMR(Integer doctorID,Integer level);

}
