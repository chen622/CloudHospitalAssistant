package cn.neuedu.his.mapper;

import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface MedicalRecordTemplateMapper extends MyMapper<MedicalRecordTemplate> {
    public List<MedicalRecordTemplate> getHospitalMR(Integer doctorID, Integer level) ;

    public List<MedicalRecordTemplate> getDeptMR(Integer doctorID,Integer level) ;

    public List<MedicalRecordTemplate> getPersonalMR(Integer doctorID,Integer level);

    List<MedicalRecordTemplate> getMeicalRecordTemByName(String name);
}